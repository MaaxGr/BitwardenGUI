import de.maaxgr.passwordmanager.BitwardenPasswordProvider
import de.maaxgr.passwordmanager.util.CommandLineExecutor
import org.junit.Test
import java.io.FileReader

class Test {

    @Test
    fun test() {
        val provider = BitwardenPasswordProvider("")
        //val passwords = provider.loadPasswords()

        //println(passwords.size)

        //passwords.forEach { println(it.name) }

    }

    @Test
    fun testCle() {

        val result = CommandLineExecutor.run("bw list items --session  >> /tmp/test.txt")

        val reader = FileReader("/tmp/test.txt")

        val string = reader.readText()
        println(string)
        println(result)

    }

}