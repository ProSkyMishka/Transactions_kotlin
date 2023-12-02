import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

fun validatePercent(value: Int) {
    if (value > 100 || value < 0)
        throw IllegalStateException("Percent incorrect")
}

class ReturnMoneyNotAllowedException(
    private val returnDate: LocalDate,
    cause: Throwable? = null
) : Exception(cause) {

    override val message: String
        get() = "Money can't be return for SafeAccount until $returnDate"
}

class OutOfMoneyLimitException(
    private val limit: BigDecimal,
    cause: Throwable? = null
) : Exception(cause) {

    override val message: String
        get() = "You can't withdraw money more than $limit"
}

abstract class Account(

    val name: String,
    protected var money: BigDecimal = BigDecimal.ZERO
) {
    val trans: MutableMap<LocalDateTime, MutableList<String>> = mutableMapOf()

    open fun getTransations(from: LocalDateTime, to: LocalDateTime) {
        for (i in trans) {
            if (i.key >= from && i.key <= to) {
                println("${i.key.toString()}: ${i.value}")
            }
        }
    }

    open fun addMoney(value: BigDecimal) {
        if (value <= BigDecimal.ZERO) {
            throw IllegalStateException("You can't add negative value or zero")
        }
        var date: LocalDateTime = LocalDateTime.now()
        money += value
        if (trans.containsKey(date)) {
            trans[date]?.add("add ${value} Money")
        }
        else {
            val temp: MutableList<String> = mutableListOf<String>("add ${value} Money")
            trans.put(date, temp)
        }
    }

    open fun withdrawMoney(value: BigDecimal): BigDecimal {
        if (value <= BigDecimal.ZERO)
            throw IllegalStateException("You can't withdraw negative money")
        var date: LocalDateTime = LocalDateTime.now()
        if (trans.containsKey(date)) {
            trans[date]?.add("withdraw ${value} Money")
        }
        else {
            val temp: MutableList<String> = mutableListOf<String>("withdraw ${value} Money")
            trans.put(date, temp)
        }
        return value
    }
}

class SafeAccount(
    name: String,
    money: BigDecimal = BigDecimal.ZERO,
    private val createDate: LocalDate = LocalDate.now(),
    private val returnDate: LocalDate = createDate.plusYears(1)
) : Account(name, money) {

    fun getReturnDate() = returnDate

    fun addDays(days: Long) {
        returnDate.plusDays(days)
    }

    override fun withdrawMoney(value: BigDecimal): BigDecimal {
        super.withdrawMoney(value)

        if (LocalDate.now() < returnDate)
            throw ReturnMoneyNotAllowedException(returnDate)

        return if (money < value) {
            val returnMoney = money
            money = BigDecimal.ZERO

            returnMoney
        } else {
            money -= value

            value
        }
    }
}

class CreditAccount(
    name: String,
    money: BigDecimal = BigDecimal.ZERO,
    percent: Int,
    private val limit: BigDecimal
) : Account(name, money) {

    private var _percent: Int = percent
    var percent: Int
        get(): Int = _percent
        set(value) {
            validatePercent(value)

            _percent = value
        }

    init {
        validatePercent(percent)
    }

    override fun withdrawMoney(value: BigDecimal): BigDecimal {
        super.withdrawMoney(value)

        if (value > limit)
            throw OutOfMoneyLimitException(limit)

        val payment = value * BigDecimal(percent / 100)
        money -= value + payment

        return payment
    }
}
