class Bill() {
    var name: String = ""
    var accountNumber: Int = 1
    var balance: Double = 0.0

    companion object {
        var count = 0
    }

    constructor(_name: String) : this() {
        count++
        name = _name
        accountNumber = count
    }
}

fun get_option(): String {
    println(
        "\nВыберите один из следующих пунктов\n" +
                "1) Просмотр списка счетов\n" +
                "2) Открытие нового счета\n" +
                "3) Пополнение счета\n" +
                "4) Перевод денег между счетами\n" +
                "5) Выход\n"
    )
    return readln()
}

fun menu(): Int {
    val flag: Boolean = true
    while (flag) {
        val opt = get_option()
        if ((opt.toIntOrNull() != null) && (opt.toInt() in 1..5)) {
            return opt.toInt()
        } else {
            println("\n Неправильный ввод! Нужно ввести число от 1 до 5")
        }
    }
    return 1
}

fun createBill(accounts: ArrayList<Bill>) {
    println("\n Введите имя счета или 0 чтобы выйти в главное меню\n")
    var name: String = readln()
    while (name.isEmpty()) {
        println("\nИмя должно содержать хотя бы 1 символ")
        name = readln()
    }
    if (name == "0")
        return
    println("\nСчет успешно создан")
    accounts.add(Bill(name))
}

fun printAllBills(accounts: ArrayList<Bill>) {
    if (accounts.size == 0) {
        println("\nНа данный момент нет открытых счетов")
        return
    }
    println()
    accounts.forEach { it -> println("Счет ${it.name} №${it.accountNumber} Баланс: ${it.balance}") }
}

fun readBillNumber(accounts: ArrayList<Bill>): Int {
    if (accounts.size == 0) {
        println("\n На данным момент нет открытых счетов. Чтобы открыть счет нажмите 2 в главном меню\n")
        return 0
    }
    val flag: Boolean = true
    var num: String
    val lastBill = accounts[accounts.size - 1].accountNumber
    println("\nВведите номер пополняемого счета или 0 чтобы выйти в меню")
    while (flag) {
        num = readln()
        if (num.toIntOrNull() != null && (num.toInt() in 1..lastBill || num.toInt() == 0)) {
            if (num.toInt() in 1..lastBill)
                return num.toInt()
            else
                return 0
        } else {
            println(
                "\nТакого номера счета не существует! Доступные номера счетов: с 1 по $lastBill" +
                        "\nВведите корректный номер или 0 - чтобы выйти"
            )
        }
    }
    return 0
}

fun replenishment(accounts: ArrayList<Bill>) {
    val num = readBillNumber(accounts)
    if (num == 0)
        return
    println("\n Введите сумму для пополнеия или 0 чтобы выйти в главное меню")
    while (true) {
        val money = readln()
        if (money.toDoubleOrNull() != null && money.toDouble() >= 0) {
            if (money.toDouble() == 0.0)
                return
            else {
                accounts[num - 1].balance += money.toDouble()
                println("\nПополнение успешно совершено!\n")
                return
            }
        } else
            println("\n Неправильный формат ввода! Попробуйте ввести еще раз или 0 чтобы выйти в главное меню")
    }
}

fun transfer(accounts: ArrayList<Bill>) {
    val fromBill = readBillNumber(accounts)
    if (fromBill == 0)
        return
    val toBill = readBillNumber(accounts)
    if (toBill == 0)
        return
    println("\nВведите сумму перевода или 0 чтобы выйти в главное меню\n")
    while (true) {
        val money = readln()
        if (money.toDoubleOrNull() != null && money.toDouble() >= 0) {
            if (money.toDouble() == 0.0)
                return
            else if (accounts[fromBill-1].balance<money.toDouble())
                println("\nНа данном счете недостаточно средств для перевода. Введити другую сумму или 0 - чтобы выйти в меню")
            else {
                accounts[fromBill - 1].balance -= money.toDouble()
                accounts[toBill - 1].balance += money.toDouble()
                println("\nТранзакция успешно совершена!\n")
                return
            }
        } else
            println("\n Неправильный формат ввода! Попробуйте ввести еще раз или 0 чтобы выйти в главное меню")
    }
}

fun main() {
    val accounts: ArrayList<Bill> = arrayListOf()
    val flag: Boolean = true
    while (flag) {
        val res = menu()
        when (res) {
            1 -> printAllBills(accounts)
            2 -> createBill(accounts)
            3 -> replenishment(accounts)
            4 -> transfer(accounts)
            5 -> {println("\nПока, надеюсь ты еще вернешься!")
                break}
        }
    }
}