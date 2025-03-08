package cn.nukkit.console

import cn.nukkit.Player
import cn.nukkit.Server
import lombok.RequiredArgsConstructor
import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine
import java.util.*
import java.util.function.Consumer

@RequiredArgsConstructor
class NukkitConsoleCompleter : Completer {
    private val server: Server? = null

    override fun complete(lineReader: LineReader, parsedLine: ParsedLine, candidates: MutableList<Candidate>) {
        if (parsedLine.wordIndex() == 0) {
            if (parsedLine.word().isEmpty()) {
                addCandidates { s: String? -> candidates.add(Candidate(s)) }
                return
            }
            val names: SortedSet<String> = TreeSet()
            addCandidates { e: String -> names.add(e) }
            for (match in names) {
                if (!match.lowercase().startsWith(parsedLine.word())) {
                    continue
                }

                candidates.add(Candidate(match))
            }
        } else if (parsedLine.wordIndex() > 0 && !parsedLine.word().isEmpty()) {
            val word = parsedLine.word()
            val names: SortedSet<String> = TreeSet()
            server!!.onlinePlayers.values.forEach(Consumer<Player> { p: Player -> names.add(p.getName()) })
            for (match in names) {
                if (!match.lowercase().startsWith(word.lowercase())) {
                    continue
                }

                candidates.add(Candidate(match))
            }
        }
    }

    private fun addCandidates(commandConsumer: Consumer<String>) {
        for (command in server!!.commandMap.commands.keys) {
            if (!command.contains(":")) {
                commandConsumer.accept(command)
            }
        }
    }
}
