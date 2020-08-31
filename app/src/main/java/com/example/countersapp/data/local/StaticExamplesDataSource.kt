package com.example.countersapp.data.local

import com.example.countersapp.ui.models.Example
import io.reactivex.rxjava3.core.Single

class StaticExamplesDataSource : ExamplesDataSource {
    override fun getExamples(): Single<List<Example>> {
        return Single.just(examples)
    }

    companion object {
        val examples = listOf(
            Example(
                "Drinks",
                listOf("Cups of coffee", "Glasses of water", "Glasses of milk", "Cups of wine")
            ),
            Example(
                "Food",
                listOf(
                    "Hot-dogs",
                    "Cupcakes eaten",
                    "Chicken salad",
                    "Burgers",
                    "Meat sandwich"
                )
            ),
            Example(
                "Hobbies",
                listOf("Songs played", "Goals on FIFA", "Kills in COD", "Warzone wins")
            ),
            Example(
                "Mis",
                listOf("Times sneezed", "Naps", "Day dreaming", "Lines of code", "App Crashes")
            )
        )
    }
}