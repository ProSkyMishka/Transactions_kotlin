import org.w3c.dom.ranges.Range
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun main(args: Array<String>) {

    val account1 = SafeAccount(
        name ="Safe1",
        money = BigDecimal.TEN,
        createDate = LocalDate.now().minusYears(1)
    )
    val date = LocalDateTime.now()
    account1.addMoney(BigDecimal(124))
    account1.withdrawMoney(BigDecimal(100))
    println("Print how many moneys you want to withdraw: ")
    account1.withdrawMoney(BigDecimal(readln()))
    val date1 = LocalDateTime.now()
    println("transactions in period from ${date.truncatedTo(ChronoUnit.SECONDS)} till now:")
    account1.getTransations(date, LocalDateTime.now())
    println("\ntransactions in period from ${date1.truncatedTo(ChronoUnit.SECONDS)} till now:")
    account1.getTransations(date1, LocalDateTime.now())
}
