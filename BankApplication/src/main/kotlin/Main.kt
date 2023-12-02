import org.w3c.dom.ranges.Range
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

fun main(args: Array<String>) {

    val account1 = SafeAccount(
        name ="Safe1",
        money = BigDecimal.TEN,
        createDate = LocalDate.now().minusYears(1)
    )
    val date = LocalDateTime.now()
    account1.addMoney(BigDecimal(124))
    val date1 = LocalDateTime.now()
    account1.withdrawMoney(BigDecimal(100))
    println("transactions in period from date till now:")
    account1.getTransations(date, LocalDateTime.now())
    println("\ntransactions in period from date1 till now:")
    account1.getTransations(date1, LocalDateTime.now())
}
