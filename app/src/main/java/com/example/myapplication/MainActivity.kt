package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText: EditText = findViewById(R.id.editText)
        val button: Button = findViewById(R.id.button)
        val scrollView: ScrollView = findViewById(R.id.scrollView)
        val textView: TextView = findViewById(R.id.textView)

        button.setOnClickListener {
            val numCars = editText.text.toString().toInt()
            if (numCars > 0) {
                val cars = createCars(numCars)
                val winner = race(cars)
                textView.text = "Победитель: ${winner.name}\n${getRaceLog()}"
                scrollView.smoothScrollTo(0, textView.bottom)
            } else {
                textView.text = "Неверное количество автомобилей"
            }
        }
    }

    private fun createCars(count: Int): List<Car> {
        val cars = mutableListOf<Car>()
        for (i in 1..count) {
            val carType = getRandomCarType()
            val model = "Model ${i}"
            val year = Random.nextInt(1990, 2024)
            val drive = getRandomDrive()
            val horsepower = Random.nextInt(100, 300)
            cars.add(
                when (carType) {
                    CarType.Sedan -> Sedan(
                        name = "$i",
                        model = model,
                        year = year,
                        drive = drive,
                        horsepower = horsepower,
                        trunkCapacity = Random.nextInt(400, 600)
                    )
                    CarType.SUV -> SUV(
                        name = "$i",
                        model = model,
                        year = year,
                        drive = drive,
                        horsepower = horsepower,
                        passengerCapacity = Random.nextInt(5, 8)
                    )
                    CarType.SportsCar -> SportsCar(
                        name = "$i",
                        model = model,
                        year = year,
                        drive = drive,
                        horsepower = horsepower,
                        topSpeed = Random.nextInt(250, 350)
                    )

                    CarType.PickUp -> PickUp(
                    name = "$i",
                    model = model,
                    year = year,
                    drive = drive,
                    horsepower = horsepower,
                    clearance = Random.nextInt(180, 250)
                )
                }
            )
        }
        return cars
    }

    private fun race(cars: List<Car>): Car {
        var currentCars = cars.toMutableList()
        while (currentCars.size > 1) {
            val nextRound = mutableListOf<Car>()
            if (currentCars.size % 2 != 0) {
                val luckyCar = currentCars.random()
                raceLog.add("--- ${luckyCar.name} - Проходит в следующий круг без гонки")
                nextRound.add(luckyCar)
                currentCars.remove(luckyCar)
            }

            while (currentCars.size >= 2) {
                val car1 = currentCars.random()
                currentCars.remove(car1)
                val car2 = currentCars.random()
                currentCars.remove(car2)

                val winner = compareCars(car1, car2)
                raceLog.add("--- Гонка между ${car1.name} и ${car2.name}, Победитель ${winner.name}")
                nextRound.add(winner)
            }
            currentCars = nextRound
        }
        return currentCars[0]
    }


    private fun compareCars(car1: Car, car2: Car): Car {
        val comparison = listOf(
            compareHorsepower(car1, car2)
        )
        for (result in comparison) {
            if (result != 0) {
                return if (result > 0) car1 else car2
            }
        }
        return car1
    }

    private fun compareHorsepower(car1: Car, car2: Car): Int {
        return car1.horsepower.compareTo(car2.horsepower)
    }

    private fun getRandomDrive(): String {
        val drives = listOf("передний", "задний", "полный")
        return drives.random()
    }

    private fun getRandomCarType(): CarType {
        val types = listOf(CarType.Sedan, CarType.SUV, CarType.SportsCar, CarType.PickUp)
        return types.random()
    }

    private val raceLog = mutableListOf<String>()
    private fun getRaceLog(): String {
        return raceLog.joinToString("\n")
    }
}

enum class CarType { Sedan, SUV, SportsCar, PickUp }

abstract class Car(
    val name: String,
    val model: String,
    val year: Int,
    val drive: String,
    val horsepower: Int
) {
    abstract fun printInfo(): String
}

class Sedan(
    name: String,
    model: String,
    year: Int,
    drive: String,
    horsepower: Int,
    val trunkCapacity: Int
) : Car(name, model, year, drive, horsepower) {
    override fun printInfo(): String {
        return "Название: $name, Модель: $model, Год выпуска: $year, Привод: $drive, Мощность: $horsepower, Объем багажника: $trunkCapacity л"
    }
}

class SUV(
    name: String,
    model: String,
    year: Int,
    drive: String,
    horsepower: Int,
    val passengerCapacity: Int
) : Car(name, model, year, drive, horsepower) {
    override fun printInfo(): String {
        return "Название: $name, Модель: $model, Год выпуска: $year, Привод: $drive, Мощность: $horsepower, Вместимость: $passengerCapacity чел"
    }
}

class SportsCar(
    name: String,
    model: String,
    year: Int,
    drive: String,
    horsepower: Int,
    val topSpeed: Int
) : Car(name, model, year, drive, horsepower) {
    override fun printInfo(): String {
        return "Название: $name, Модель: $model, Год выпуска: $year, Привод: $drive, Мощность: $horsepower, Максимальная скорость: $topSpeed км/ч"
    }
}

class PickUp(
    name: String,
    model: String,
    year: Int,
    drive: String,
    horsepower: Int,
    val clearance: Int
) : Car(name, model, year, drive, horsepower) {
    override fun printInfo(): String {
        return "Название: $name, Модель: $model, Год выпуска: $year, Привод: $drive, Мощность: $horsepower, Клиренс: $clearance мм"
    }
}
