package org.chorus.utils

import java.io.Serial
import java.io.Serializable
import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * SortedList是[List]的一个有序实现，内部是用平衡二叉树实现的。
 *
 *
 * 请注意，你不能对这个列表做指定项操作，除了`remove(int)`，`remove(Object)`，`clear`和`add()`。
 *
 *
 * 此列表操作的时间复杂度如下：`contains`，`add`，`remove`以及`get`
 * 的时间复杂度为*O(log(n))*。
 *
 *
 * 此列表不保证线程安全，若有必要，请使用[Collections.synchronizedList]包装以确保线程安全。
 *
 *
 * 这个列表提供的迭代器是快速失效的，所以除了通过迭代器本身之外的任何结构修改都会导致它抛出[ConcurrentModificationException]。
 *
 * @param <T> 列表类型.
 * @see List
 *
 * @see Collection
 *
 * @see AbstractList
</T> */
open class SortedList<T>(// 元素排序比较器
    private val comparator: Comparator<in T>
) : AbstractList<T>(), Serializable {
    // 用以获取下一个节点的id
    private var nextNodeID = Int.MIN_VALUE

    // 平衡树的根节点
    private var root: Node? = null

    /**
     * 将给定对象插入SortedList的适当位置，以确保列表中的元素按给定比较器指定的顺序保存。
     *
     *
     * 此方法仅允许添加非null值，如果给定对象为null，则列表保持不变并返回false。
     *
     * @param element 要添加的元素
     * @return 当给定对象为null时为false，否则为true
     */
    override fun add(element: T): Boolean {
        var treeAltered = false
        if (element != null) {
            // 将值包装在节点中并添加它到树上
            add(Node(element)) //这将确保modcount自增
            treeAltered = true
        }
        return treeAltered
    }

    /**
     * 将给定节点添加到此SortedList。
     *
     *
     * 此方法可以被子类重写，以便更改此列表将存储的节点的定义。
     *
     *
     * 此实现使用[Node.compareTo]方法来确定给定节点应该存储在哪里。它还会增加此列表的modCount。
     *
     * @param toAdd 要新增的节点
     */
    protected fun add(toAdd: Node) {
        if (root == null) {
            root = toAdd
        } else {
            var current: Node? = root
            while (current != null) { // 理论上这玩意==true，但是为了确保使用动态代理、JVMTI、调试器或JVMCI时仍然能正常，我们需要判断下
                val comparison: Int = toAdd.compareTo(current)
                current = if (comparison < 0) { // toAdd < node
                    if (current.leftChild == null) {
                        current.setLeftChild(toAdd)
                        break
                    } else {
                        current.leftChild
                    }
                } else { // toAdd > node （==应该不太可能会发生，即使发生了上面的逃生门也能发挥作用）
                    if (current.rightChild == null) {
                        current.setRightChild(toAdd)
                        break
                    } else {
                        current.rightChild
                    }
                }
            }
        }
        modCount++ // 参阅AbstractList#modCount，增加这个值可以使迭代器快速失效。
    }

    /**
     * 测试是否此树与给定树的结构和值完全相同。仅供测试使用。
     */
    fun structurallyEqualTo(other: SortedList<T>?): Boolean {
        return other != null && structurallyEqualTo(root, other.root)
    }

    private fun structurallyEqualTo(currentThis: Node?, currentOther: Node?): Boolean {
        if (currentThis == null) {
            return currentOther == null
        } else if (currentOther == null) {
            return false
        }
        return currentThis.value == currentOther.value
                && structurallyEqualTo(currentThis.leftChild, currentOther.leftChild)
                && structurallyEqualTo(currentThis.rightChild, currentOther.rightChild)
    }

    /**
     * 提供一个迭代器，该迭代器按照给定比较器确定的顺序提供此SortedList的元素。
     *
     *
     * 此迭代器实现允许以O(n)时间复杂度迭代整个列表，其中n是列表中的元素数。
     *
     * @return 一个按照给定比较器确定的顺序提供这个分类列表的元素的迭代器。
     */
    override fun iterator(): MutableIterator<T> {
        return Itr()
    }

    // 使用后继方法的迭代器接口的实现
    // 为了提高速度至O(n)，我们通过列表进行迭代，而不是O(n*log(n))的排序。
    private inner class Itr : MutableIterator<T> {
        private var nextNode: Node? = (if (isEmpty()) null else findNodeAtIndex(0))
        private var nextIndex = 0
        private var lastReturned: Node? = null

        /**
         * 此迭代器预期的modCount
         */
        private var expectedModCount = modCount

        override fun hasNext(): Boolean {
            return nextNode != null
        }

        override fun next(): T {
            checkModCount()

            if (nextNode == null) {
                throw NoSuchElementException()
            }

            lastReturned = nextNode!!
            nextNode = nextNode!!.successor()
            nextIndex++

            return lastReturned!!.value
        }

        override fun remove() {
            checkModCount()

            checkNotNull(lastReturned)

            this@SortedList.remove(lastReturned!!)
            lastReturned = null

            // 下一个节点现在可能不正确，所以需要再次获取它。
            nextIndex--
            nextNode = if (nextIndex < size) { // 检查具有此索引的节点是否确实存在。
                findNodeAtIndex(nextIndex)
            } else {
                null
            }

            expectedModCount = modCount
        }

        /**
         * 检查modcount是否为预期值
         */
        fun checkModCount() {
            if (expectedModCount != modCount) {
                throw ConcurrentModificationException()
            }
        }
    }

    /**
     * @return 存储在此SortedList中的元素数量。
     */
    override val size: Int
        get() {
            return if (root == null) 0 else 1 + root!!.numChildren
        }

    /**
     * @return 此SortedList的根节点，如果此列表为空，则为空。
     */
    protected fun getRoot(): Node? {
        return root
    }

    /**
     * 返回给定对象是否在此SortedList中。
     *
     *
     * 元素比较使用[Object.equals]方法，并假设给定的obj必须具有与此SortedList中的元素相等的T类型。
     * 时间复杂度为*O(log(n))*，其中n是列表中的元素数。
     *
     * @param element 要检查存在性的对象
     * @return 是否存在于此SortedList中
     */
    override fun contains(element: T): Boolean {
        return element != null && !isEmpty() && findFirstNodeWithValue(element) != null
    }

    /**
     * 返回表示树中给定值的节点，如果不存在此类节点，则该节点可以为null。
     *
     *
     * 该方法使用给定的比较器执行二进制搜索，时间复杂度为O(log(n))。
     *
     * @param value 要搜索的值
     * @return 此列表中具有给定值的第一个节点
     */
    protected fun findFirstNodeWithValue(value: T): Node? {
        var current: Node? = root
        while (current != null) {
            val comparison = comparator.compare(current.value, value)
            if (comparison == 0) {
                while (current!!.leftChild != null
                    && comparator.compare(current.leftChild!!.value, value) == 0
                ) {
                    current = current.leftChild
                }
                break
            } else if (comparison < 0) {
                current = current.rightChild
            } else {
                current = current.leftChild
            }
        }
        return current
    }

    /**
     * 移除并返回此SortedList中给定索引处的元素。由于列表已排序，这是从0-n-1开始计数的第四个最小元素。
     *
     *
     * 例如，调用remove(0)将删除列表中最小的元素。
     *
     * @param index 要删除的元素的索引
     * @return 被删除的元素
     * @throws IllegalArgumentException 如果索引不是有效的索引则抛出此异常
     */
    override fun removeAt(index: Int): T {
        // 在索引处获取节点，如果节点索引不存在，将抛出异常。
        val nodeAtIndex: Node = findNodeAtIndex(index)
        remove(nodeAtIndex)
        return nodeAtIndex.value
    }

    /**
     * 删除列表中具有给定值的第一个元素（如果存在这样的节点），否则不执行任何操作。使用给定的比较器对元素进行比较。
     *
     *
     * 返回是否找到并删除了匹配的元素。
     *
     * @param element 要移除的元素
     * @return 是否找到并删除了匹配的元素。
     */
    override fun remove(element: T): Boolean {
        var treeAltered = false
        try {
            if (element != null && root != null) {
                val toRemove: Node? = findFirstNodeWithValue(element)
                if (toRemove != null) {
                    remove(toRemove)
                    treeAltered = true
                }
            }
        } catch (ignore: ClassCastException) {
        }
        return treeAltered
    }

    /**
     * 从这个SortedList中删除给定的节点，如果需要重新平衡，也会添加modCount。
     * 时间复杂度O(log(n))。
     *
     * @param toRemove 此SortedList中的节点
     */
    protected fun remove(toRemove: Node) {
        if (toRemove.isLeaf) {
            val parent: Node? = toRemove.parent
            if (parent == null) {
                root = null
            } else {
                toRemove.detachFromParentIfLeaf()
            }
        } else if (toRemove.hasTwoChildren()) {
            val successor: Node = toRemove.successor()!!
            toRemove.switchValuesForThoseIn(successor)
            remove(successor)
        } else if (toRemove.leftChild != null) {
            toRemove.leftChild!!.contractParent()
        } else {
            toRemove.rightChild!!.contractParent()
        }
        modCount++
    }

    /**
     * 返回此SortedList中给定索引处的元素。由于列表已排序，因此这是从0-n-1开始计算的第四个最小元素的索引。
     *
     *
     * 例如，调用get(0)将返回列表中最小的元素。
     *
     * @param index 要获取的元素的索引
     * @return 此SortedList中给定索引处的元素
     * @throws IllegalArgumentException 如果索引不是有效的索引则抛出此异常
     */
    override fun get(index: Int): T {
        return findNodeAtIndex(index).value
    }

    protected fun findNodeAtIndex(index: Int): Node {
        require(!(index < 0 || index >= size)) { "$index is not valid index." }
        var current: Node? = root
        var totalSmallerElements = if (current?.leftChild == null) 0 else current.leftChild!!.sizeOfSubTree()
        while (current != null) {
            if (totalSmallerElements == index) {
                break
            }
            if (totalSmallerElements > index) {
                current = current.leftChild
                totalSmallerElements--
                totalSmallerElements -= if (Objects.requireNonNull<Node?>(current).rightChild == null) 0 else current!!.rightChild!!.sizeOfSubTree()
            } else {
                totalSmallerElements++
                current = current.rightChild
                totalSmallerElements += if (current?.leftChild == null) 0 else current.leftChild!!.sizeOfSubTree()
            }
        }
        return current!!
    }

    override fun isEmpty(): Boolean {
        return root == null
    }

    override fun clear() {
        root = null
    }

    override fun toArray(): Array<Any?> {
        val array = arrayOfNulls<Any>(size)
        var positionToInsert = 0
        if (root != null) {
            var next: Node? = root!!.smallestNodeInSubTree()
            while (next != null) {
                array[positionToInsert] = next.value
                positionToInsert++

                next = next.successor()
            }
        }
        return array
    }

    /**
     * 返回整个列表中的最小平衡因子。仅供测试使用
     */
    fun minBalanceFactor(): Int {
        var minBalanceFactor = 0
        var current: Node? = root
        while (current != null) {
            minBalanceFactor = min(current.balanceFactor.toDouble(), minBalanceFactor.toDouble()).toInt()
            current = current.successor()
        }
        return minBalanceFactor
    }

    /**
     * 返回整个列表中的最大平衡因子。仅供测试使用
     */
    fun maxBalanceFactor(): Int {
        var maxBalanceFactor = 0
        var current: Node? = root
        while (current != null) {
            maxBalanceFactor = max(current.balanceFactor.toDouble(), maxBalanceFactor.toDouble()).toInt()
            current = current.successor()
        }
        return maxBalanceFactor
    }

    //从startNode开始执行二叉树的再平衡，并向上递归树。..
    private fun rebalanceTree(startNode: Node) {
        var current: Node? = startNode
        while (current != null) {
            //获取此时左右子树之间的差异。
            val balanceFactor: Int = current.balanceFactor

            if (balanceFactor == -2) {
                if (current.rightChild!!.balanceFactor == 1) {
                    current.rightChild!!.leftChild!!.rightRotateAsPivot()
                }
                current.rightChild!!.leftRotateAsPivot()
            } else if (balanceFactor == 2) {
                if (current.leftChild!!.balanceFactor == -1) {
                    current.leftChild!!.rightChild!!.leftRotateAsPivot()
                }
                current.leftChild!!.rightRotateAsPivot()
            }

            if (current.parent == null) {
                root = current
                break
            } else {
                current = current.parent!!
            }
        }
    }

    protected open inner class Node(t: T) : Comparable<Node> {
        var value: T
            private set

        var leftChild: Node? = null
        var rightChild: Node? = null
        var parent: Node? = null

        private var height = 0
        var numChildren = 0
            private set

        /**
         * 此节点的唯一id：自动生成，节点越新，此值越高。
         */
        protected val id: Int

        init {
            this.value = t
            this.id = nextNodeID++
        }

        fun hasTwoChildren(): Boolean {
            return leftChild != null && rightChild != null
        }

        // 如果是叶节点，则删除该节点，并更新树中的子节点数和高度。
        fun detachFromParentIfLeaf() {
            if (!isLeaf || parent == null) {
                throw RuntimeException("Call made to detachFromParentIfLeaf, but this is not a leaf node with a parent!")
            }
            if (isLeftChildOfParent) {
                parent!!.setLeftChild(null)
            } else {
                parent!!.setRightChild(null)
            }
        }

        protected val grandParent: Node?
            /**
             * 返回此节点的父节点，该节点可能为空。
             *
             * @return 此节点的父节点（如果存在），否则为null
             */
            get() = if (parent != null && parent!!.parent != null) parent!!.parent else null

        // 将此节点在树上向上移动一个槽口，更新值并重新平衡树。
        fun contractParent() {
            if (parent == null || parent!!.hasTwoChildren()) {
                throw RuntimeException("Can not call contractParent on root node or when the parent has two children!")
            }
            val grandParent: Node? = grandParent
            if (grandParent != null) {
                if (isLeftChildOfParent) {
                    if (parent!!.isLeftChildOfParent) {
                        grandParent.leftChild = this
                    } else {
                        grandParent.rightChild = this
                    }
                    parent = grandParent
                } else {
                    if (parent!!.isLeftChildOfParent) {
                        grandParent.leftChild = this
                    } else {
                        grandParent.rightChild = this
                    }
                    parent = grandParent
                }
            } else {
                parent = null
                root = this // 如果在其他地方没有进行更新就重设根
            }

            // 最后，更新值并重新平衡这颗平衡二叉树。
            updateCachedValues()
            rebalanceTree(this)
        }

        val isLeftChildOfParent: Boolean
            /**
             * 返回它是否是其父节点的左子节点；如果这是根节点，则返回false。
             *
             * @return 如果这是其父节点的左子节点，则为true，否则为false
             */
            get() = parent != null && parent!!.leftChild === this

        val isRightChildOfParent: Boolean
            /**
             * 返回它是否是其父节点的右子节点；如果这是根节点，则返回false。
             *
             * @return 如果这是其父节点的右子节点，则为true，否则为false
             */
            get() = parent != null && parent!!.rightChild === this

        protected fun getLeftChild(): Node? {
            return leftChild
        }

        protected fun getRightChild(): Node? {
            return rightChild
        }

        protected fun getParent(): Node? {
            return parent
        }

        /**
         * 使用比较器将存储在该节点上的值与给定节点上的值进行比较，如果这些值相等，则比较其ID上的节点。我们认定较老的节点较小。
         *
         * @return 如果比较器在比较存储在该节点和给定节点上的值时返回一个非零数字，则返回该数字，否则返回该节点的id减去给定节点的id
         */
        override fun compareTo(other: Node): Int {
            val comparison = comparator.compare(value, other.value)
            return if (comparison == 0) (id - other.id) else comparison
        }

        fun smallestNodeInSubTree(): Node {
            var current: Node = this
            while (true) {
                if (current.leftChild == null) {
                    break
                } else {
                    current = current.leftChild!!
                }
            }
            return current
        }

        protected fun largestNodeInSubTree(): Node {
            var current: Node = this
            while (true) {
                if (current.rightChild == null) {
                    break
                } else {
                    current = current.rightChild!!
                }
            }
            return current
        }

        /**
         * 获取树中下一个最大的节点，如果这是值最大的节点，则为null。
         *
         * @return 树中下一个最大的节点，如果这是值最大的节点，则为null
         */
        fun successor(): Node? {
            var successor: Node? = null
            if (rightChild != null) {
                successor = rightChild!!.smallestNodeInSubTree()
            } else if (parent != null) {
                var current: Node = this
                while (current.isRightChildOfParent) {
                    current = current.parent!!
                }
                successor = Objects.requireNonNull(current).parent
            }
            return successor
        }

        /**
         * 获取树中下一个最小的节点，如果这是值最小的节点，则为null。
         *
         * @return 树中下一个最小的节点，如果这是值最小的节点，则为null
         */
        protected fun predecessor(): Node? {
            var predecessor: Node? = null
            if (leftChild != null) {
                predecessor = leftChild!!.largestNodeInSubTree()
            } else if (parent != null) {
                var current: Node = this
                while (current.isLeftChildOfParent) {
                    current = current.parent!!
                }
                predecessor = Objects.requireNonNull(current).parent
            }
            return predecessor
        }

        // 将子节点设置为左/右，仅当给定节点为null或叶，且当前子节点相同时才应如此
        private fun setChild(isLeft: Boolean, leaf: Node?) {
            //perform the update.
            if (leaf != null) {
                leaf.parent = this
            }
            if (isLeft) {
                leftChild = leaf
            } else {
                rightChild = leaf
            }

            // 确保树高的任何变化都得到了处理。
            updateCachedValues()
            rebalanceTree(this)
        }

        val isLeaf: Boolean
            /**
             * 返回此节点是否为叶节点，如果其左和右子级都设置为null，则它就是叶子~~姐姐~~。
             *
             * @return 如果此节点为叶节点，则为true，否则为false。
             */
            get() = (leftChild == null && rightChild == null)

        override fun toString(): String {
            return "[Node: value: " + value +
                    ", leftChild value: " + (if (leftChild == null) "null" else leftChild!!.value) +
                    ", rightChild value: " + (if (rightChild == null) "null" else rightChild!!.value) +
                    ", height: " + height +
                    ", numChildren: " + numChildren + "]\n"
        }

        // 使用当前节点作为轴左旋。
        fun leftRotateAsPivot() {
            if (parent == null || parent!!.rightChild !== this) {
                throw RuntimeException("Can't left rotate as pivot has no valid parent node.")
            }

            // 首先将此节点向上移动，分离父节点。
            val oldParent: Node = parent!!
            val grandParent: Node? = grandParent
            if (grandParent != null) {
                if (parent!!.isLeftChildOfParent) {
                    grandParent.leftChild = this
                } else {
                    grandParent.rightChild = this
                }
            }

            this.parent = grandParent

            val oldLeftChild: Node? = leftChild
            oldParent.parent = this
            leftChild = oldParent
            if (oldLeftChild != null) {
                oldLeftChild.parent = oldParent
            }
            oldParent.rightChild = oldLeftChild

            oldParent.updateCachedValues()
        }

        /**
         * 返回此节点的子节点数加一。此方法使用缓存的变量，确保它在恒定时间内运行。
         *
         * @return 此节点的子节点数加一
         */
        fun sizeOfSubTree(): Int {
            return 1 + numChildren
        }

        fun rightRotateAsPivot() {
            if (parent == null || parent!!.leftChild !== this) {
                throw RuntimeException("Can't right rotate as pivot has no valid parent node.")
            }

            val oldParent: Node = parent!!
            val grandParent: Node? = grandParent
            if (grandParent != null) {
                if (parent!!.isLeftChildOfParent) {
                    grandParent.leftChild = this
                } else {
                    grandParent.rightChild = this
                }
            }
            this.parent = grandParent

            oldParent.parent = this
            val oldRightChild: Node? = rightChild
            rightChild = oldParent
            if (oldRightChild != null) {
                oldRightChild.parent = oldParent
            }
            oldParent.leftChild = oldRightChild

            oldParent.updateCachedValues()
        }

        /**
         * 更新此路径上节点的高度和子节点数。还为路径上的每个节点（包括此节点）调用[.updateAdditionalCachedValues]
         */
        protected fun updateCachedValues() {
            var current: Node? = this
            while (current != null) {
                if (current.isLeaf) {
                    current.height = 0
                    current.numChildren = 0
                } else {
                    val leftTreeHeight = if (current.leftChild == null) 0 else current.leftChild!!.height
                    val rightTreeHeight = if (current.rightChild == null) 0 else current.rightChild!!.height
                    current.height = 1 + max(leftTreeHeight, rightTreeHeight)

                    val leftTreeSize = if (current.leftChild == null) 0 else current.leftChild!!.sizeOfSubTree()
                    val rightTreeSize = if (current.rightChild == null) 0 else current.rightChild!!.sizeOfSubTree()
                    current.numChildren = leftTreeSize + rightTreeSize
                }

                current.updateAdditionalCachedValues()

                current = current.parent
            }
        }

        /**
         * 当从树中插入或删除节点时被调用，并为子类提供一个钩子来更新其缓存值。
         *
         *
         * 每次更改列表时都会调用此方法。它首先在受给定更改影响的最深节点上调用，然后在该节点的祖先上调用，直到在根节点上调用为止。
         *
         *
         * 因此，它仅适用于在缓存值非全局且不依赖于计算时具有正确值的父节点的情况下更新缓存值。
         *
         *
         * 这个实现是空的，留给子类使用（虽然我觉得没人会用）。
         */
        protected fun updateAdditionalCachedValues() {
        }

        // 将此节点中的值替换为其他节点中的值。
        // 应该只在需要删除并且只有一个值时调用。
        fun switchValuesForThoseIn(other: Node) {
            this.value = other.value
        }

        val balanceFactor: Int
            get() = (if (leftChild == null) 0 else leftChild!!.height + 1) -
                    (if (rightChild == null) 0 else rightChild!!.height + 1)

        fun setLeftChild(leaf: Node?) {
            if ((leaf != null && !leaf.isLeaf) || (leftChild != null && !leftChild!!.isLeaf)) {
                throw RuntimeException("setLeftChild should only be called with null or a leaf node, to replace a likewise child node.")
            }
            setChild(true, leaf)
        }

        fun setRightChild(leaf: Node?) {
            if ((leaf != null && !leaf.isLeaf) || (rightChild != null && !rightChild!!.isLeaf)) {
                throw RuntimeException("setRightChild should only be called with null or a leaf node, to replace a likewise child node.")
            }
            setChild(false, leaf)
        }
    }

    companion object {
        @Serial
        private val serialVersionUID = -7115342129716877152L
    }
}