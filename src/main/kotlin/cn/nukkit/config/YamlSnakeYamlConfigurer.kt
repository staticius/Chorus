package cn.nukkit.config

import eu.okaeri.configs.configurer.Configurer
import eu.okaeri.configs.postprocessor.ConfigLineFilter
import eu.okaeri.configs.postprocessor.ConfigLineInfo
import eu.okaeri.configs.postprocessor.format.YamlSectionWalker
import eu.okaeri.configs.schema.ConfigDeclaration
import eu.okaeri.configs.schema.FieldDeclaration
import eu.okaeri.configs.schema.GenericsDeclaration
import eu.okaeri.configs.serdes.SerdesContext
import lombok.Setter
import lombok.experimental.Accessors
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.representer.Representer
import org.yaml.snakeyaml.resolver.Resolver
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import java.util.function.Consumer


@Accessors(chain = true)
class YamlSnakeYamlConfigurer : Configurer {
    private val yaml: Yaml
    private var map: MutableMap<String, Any>? = LinkedHashMap()

    @Setter
    private val commentPrefix = "# "

    constructor(yaml: Yaml, map: MutableMap<String, Any>) {
        this.yaml = yaml
        this.map = map
    }

    @JvmOverloads
    constructor(yaml: Yaml = createYaml()) {
        this.yaml = yaml
    }

    override fun getExtensions(): List<String> {
        return mutableListOf("yml", "yaml")
    }

    override fun setValue(key: String, value: Any, type: GenericsDeclaration, field: FieldDeclaration) {
        val simplified = this.simplify(value, type, SerdesContext.of(this, field), true)
        map!![key] = simplified
    }

    override fun setValueUnsafe(key: String, value: Any) {
        map!![key] = value
    }

    override fun getValue(key: String): Any {
        return map!![key]!!
    }

    override fun remove(key: String): Any {
        return map!!.remove(key)!!
    }

    override fun keyExists(key: String): Boolean {
        return map!!.containsKey(key)
    }

    override fun getAllKeys(): List<String> {
        return Collections.unmodifiableList(
            ArrayList(
                map!!.keys
            )
        )
    }

    @Throws(Exception::class)
    override fun load(inputStream: InputStream, declaration: ConfigDeclaration) {
        // try loading from input stream
        this.map = yaml.load(inputStream)
        // when no map was loaded reset with empty
        if (this.map == null) this.map = LinkedHashMap()
    }

    @Throws(Exception::class)
    override fun write(outputStream: OutputStream, declaration: ConfigDeclaration) {
        // render to string
        val contents = yaml.dump(this.map)

        // postprocess
        TrConfigPostprocessor.Companion.of(contents) // remove all current top-level comments
            .removeLines(ConfigLineFilter { line: String -> line.startsWith(commentPrefix.trim { it <= ' ' }) }) // add new comments
            .updateLinesKeys(object : YamlSectionWalker() {
                override fun update(line: String, lineInfo: ConfigLineInfo, path: List<ConfigLineInfo>): String {
                    var currentDeclaration = declaration
                    for (i in 0..<(path.size - 1)) {
                        val pathElement = path[i]
                        val field = currentDeclaration.getField(pathElement.name)
                        if (field.isEmpty) {
                            return line
                        }
                        val fieldType = field.get().type
                        if (!fieldType.isConfig) {
                            return line
                        }
                        currentDeclaration = ConfigDeclaration.of(fieldType.type)
                    }

                    val lineDeclaration = currentDeclaration.getField(lineInfo.name)
                    if (lineDeclaration.isEmpty) {
                        return line
                    }

                    val fieldComment = lineDeclaration.get().comment ?: return line

                    val comment: String = TrConfigPostprocessor.Companion.createComment(
                        this@YamlSnakeYamlConfigurer.commentPrefix,
                        fieldComment
                    )
                    return TrConfigPostprocessor.Companion.addIndent(comment, lineInfo.indent) + line
                }
            }) // add header if available
            .prependContextComment(this.commentPrefix, declaration.header) // save
            .write(outputStream)
    }

    companion object {
        private fun createYaml(): Yaml {
            val loaderOptions = LoaderOptions()
            val constructor = Constructor(loaderOptions)

            val dumperOptions = DumperOptions()
            dumperOptions.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK

            val representer = Representer(dumperOptions)
            representer.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK

            val resolver = Resolver()

            return Yaml(constructor, representer, dumperOptions, loaderOptions, resolver)
        }

        private fun <T> apply(`object`: T, consumer: Consumer<T>): T {
            consumer.accept(`object`)
            return `object`
        }
    }
}
