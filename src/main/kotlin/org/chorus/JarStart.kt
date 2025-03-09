package org.chorus

object JarStart {
    /**
     * If the user use java -jar to start the server.
     */
    var isUsingJavaJar: Boolean = false
        private set

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            Thread.currentThread().contextClassLoader.loadClass("joptsimple.OptionSpec")
        } catch (e: ClassNotFoundException) {
            // There are no libs now. It means that even logger cannot be used.
            println("No libraries detected. PowerNukkitX cannot work without them and will now exit.")
            println("Do NOT use java -jar to run PowerNukkitX!")
            println("For more information. See https://docs.powernukkitx.com")
            return
        } catch (e: NoClassDefFoundError) {
            println("No libraries detected. PowerNukkitX cannot work without them and will now exit.")
            println("Do NOT use java -jar to run PowerNukkitX!")
            println("For more information. See https://docs.powernukkitx.com")
            return
        }
        isUsingJavaJar = true
        Nukkit.main(args)
    }

    // Method to reset the state for testing
    fun resetUsingJavaJar() {
        isUsingJavaJar = false
    }
}
