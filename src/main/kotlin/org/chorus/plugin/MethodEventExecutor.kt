package org.chorus.plugin

import org.chorus.event.Event
import org.chorus.event.Listener
import org.chorus.utils.EventException
import lombok.extern.slf4j.Slf4j
import org.objectweb.asm.*
import java.lang.ref.WeakReference
import java.lang.reflect.InaccessibleObjectException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author MagicDroidX (Nukkit Project)
 */
@Slf4j
class MethodEventExecutor(val method: Method?) : EventExecutor {
    @Throws(EventException::class)
    override fun execute(listener: Listener?, event: Event) {
        try {
            val params = method!!.parameterTypes as Array<Class<Event>>
            for (param in params) {
                if (param.isAssignableFrom(event.javaClass)) {
                    method.invoke(listener, event)
                    break
                }
            }
        } catch (ex: InvocationTargetException) {
            throw EventException(if (ex.cause != null) ex.cause else ex)
        } catch (ex: ClassCastException) {
            MethodEventExecutor.log.debug("Ignoring a ClassCastException", ex)
            // We are going to ignore ClassCastException because EntityDamageEvent can't be cast to EntityDamageByEntityEvent
        } catch (t: Throwable) {
            throw EventException(t)
        }
    }

    companion object {
        val compileTime: AtomicInteger = AtomicInteger(0)

        fun compile(listenerClass: Class<out Listener?>, method: Method): EventExecutor? {
            return compile(listenerClass.classLoader, listenerClass, method)
        }

        fun compile(classLoader: ClassLoader, listenerClass: Class<out Listener?>, method: Method): EventExecutor? {
            if (!Modifier.isPublic(method.modifiers)) {
                return null
            }

            val eventClass = method.parameterTypes[0]
            val eventType = Type.getType(eventClass)
            val listenerType = Type.getType(listenerClass)
            val internalName = "cn/nukkit/plugin/PNXMethodEventExecutor$" + compileTime.incrementAndGet()

            val classWriter = ClassWriter(0)
            val fieldVisitor: FieldVisitor
            var methodVisitor: MethodVisitor
            classWriter.visit(
                Opcodes.V17,
                Opcodes.ACC_PUBLIC or Opcodes.ACC_SUPER,
                internalName,
                null,
                "java/lang/Object",
                arrayOf("cn/nukkit/plugin/EventExecutor", "cn/nukkit/plugin/CompiledExecutor")
            )
            classWriter.visitSource("EventHandler@" + method.declaringClass.name + "#" + method.name, null)
            run {
                fieldVisitor = classWriter.visitField(
                    Opcodes.ACC_PRIVATE or Opcodes.ACC_FINAL,
                    "originMethod",
                    "Ljava/lang/reflect/Method;",
                    null,
                    null
                )
                fieldVisitor.visitEnd()
            }
            run {
                methodVisitor =
                    classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "(Ljava/lang/reflect/Method;)V", null, null)
                methodVisitor.visitCode()
                val label0 = Label()
                methodVisitor.visitLabel(label0)
                methodVisitor.visitLineNumber(13, label0)
                methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
                val label1 = Label()
                methodVisitor.visitLabel(label1)
                methodVisitor.visitLineNumber(14, label1)
                methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
                methodVisitor.visitFieldInsn(
                    Opcodes.PUTFIELD,
                    internalName,
                    "originMethod",
                    "Ljava/lang/reflect/Method;"
                )
                val label2 = Label()
                methodVisitor.visitLabel(label2)
                methodVisitor.visitLineNumber(15, label2)
                methodVisitor.visitInsn(Opcodes.RETURN)
                val label3 = Label()
                methodVisitor.visitLabel(label3)
                methodVisitor.visitLocalVariable("this", "L$internalName;", null, label0, label3, 0)
                methodVisitor.visitLocalVariable("originMethod", "Ljava/lang/reflect/Method;", null, label0, label3, 1)
                methodVisitor.visitMaxs(2, 2)
                methodVisitor.visitEnd()
            }
            run {
                methodVisitor = classWriter.visitMethod(
                    Opcodes.ACC_PUBLIC,
                    "execute",
                    "(Lcn/nukkit/event/Listener;Lcn/nukkit/event/Event;)V",
                    null,
                    arrayOf("cn/nukkit/utils/EventException")
                )
                methodVisitor.visitCode()
                val label0 = Label()
                methodVisitor.visitLabel(label0)
                methodVisitor.visitVarInsn(Opcodes.ALOAD, 2)
                methodVisitor.visitTypeInsn(Opcodes.INSTANCEOF, eventType.internalName)
                val label1 = Label()
                methodVisitor.visitJumpInsn(Opcodes.IFEQ, label1)
                val label2 = Label()
                methodVisitor.visitLabel(label2)
                methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
                methodVisitor.visitTypeInsn(Opcodes.CHECKCAST, listenerType.internalName)
                methodVisitor.visitVarInsn(Opcodes.ALOAD, 2)
                methodVisitor.visitTypeInsn(Opcodes.CHECKCAST, eventType.internalName)
                methodVisitor.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    listenerType.internalName,
                    method.name,
                    "(" + eventType.descriptor + ")V",
                    false
                )
                methodVisitor.visitLabel(label1)
                methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null)
                methodVisitor.visitInsn(Opcodes.RETURN)
                val label3 = Label()
                methodVisitor.visitLabel(label3)
                methodVisitor.visitLocalVariable("this", "L$internalName;", null, label0, label3, 0)
                methodVisitor.visitLocalVariable("listener", "Lcn/nukkit/event/Listener;", null, label0, label3, 1)
                methodVisitor.visitLocalVariable("event", "Lcn/nukkit/event/Event;", null, label0, label3, 2)
                methodVisitor.visitMaxs(2, 3)
                methodVisitor.visitEnd()
            }
            run {
                methodVisitor = classWriter.visitMethod(
                    Opcodes.ACC_PUBLIC,
                    "getOriginMethod",
                    "()Ljava/lang/reflect/Method;",
                    null,
                    null
                )
                methodVisitor.visitCode()
                val label0 = Label()
                methodVisitor.visitLabel(label0)
                methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                methodVisitor.visitFieldInsn(
                    Opcodes.GETFIELD,
                    internalName,
                    "originMethod",
                    "Ljava/lang/reflect/Method;"
                )
                methodVisitor.visitInsn(Opcodes.ARETURN)
                val label1 = Label()
                methodVisitor.visitLabel(label1)
                methodVisitor.visitLocalVariable("this", "L$internalName;", null, label0, label1, 0)
                methodVisitor.visitMaxs(1, 1)
                methodVisitor.visitEnd()
            }
            classWriter.visitEnd()

            try {
                val clazz = loadClass(classLoader, classWriter.toByteArray())
                return clazz.getConstructor(Method::class.java).newInstance(method) as EventExecutor
            } catch (e: ClassNotFoundException) {
                return null
            } catch (e: NoSuchMethodException) {
                return null
            } catch (e: InvocationTargetException) {
                return null
            } catch (e: IllegalAccessException) {
                return null
            } catch (e: InstantiationException) {
                return null
            }
        }

        private var defineClassMethodRef = WeakReference<Method?>(null)

        @Throws(
            ClassNotFoundException::class,
            NoSuchMethodException::class,
            InvocationTargetException::class,
            IllegalAccessException::class,
            InaccessibleObjectException::class
        )
        private fun loadClass(loader: ClassLoader, b: ByteArray): Class<*> {
            var clazz: Class<*>
            val method: Method?
            if (defineClassMethodRef.get() == null) {
                val cls = Class.forName("java.lang.ClassLoader")
                method = cls.getDeclaredMethod(
                    "defineClass",
                    String::class.java,
                    ByteArray::class.java,
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                defineClassMethodRef = WeakReference(method)
            } else {
                method = defineClassMethodRef.get()
            }
            Objects.requireNonNull(method).isAccessible = true
            try {
                val args =
                    arrayOf<Any>("cn.nukkit.plugin.PNXMethodEventExecutor$" + compileTime.get(), b, 0, b.size)
                clazz = method!!.invoke(loader, *args) as Class<*>
            } finally {
                method!!.isAccessible = false
            }
            return clazz
        }
    }
}
