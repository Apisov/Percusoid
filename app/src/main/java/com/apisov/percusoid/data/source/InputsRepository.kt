package com.apisov.percusoid.data.source

import com.apisov.percusoid.data.Input
import com.apisov.percusoid.data.source.local.PercusoidDatabase
import io.reactivex.Single

class InputsRepository(
    private val database: PercusoidDatabase
) {
    fun saveInput(input: Input): Single<String> =
        Single.defer {
            val inputEntity = fromInputToInputEntity(input)
            Single.just(
                database.inputsDao().saveInput(inputEntity)
            ).map { inputEntity.id }
        }

    fun saveInputs(inputs: List<Input>): Single<Unit> =
        Single.defer {
            val inputsEntities = fromInputsToInputEntityList(inputs)
            Single.just(
                database.inputsDao().saveInputs(inputsEntities)
            )
        }

    fun updateInputs(inputs: List<Input>): Single<Unit> =
        Single.defer {
            val inputsEntities = fromInputsToInputEntityList(inputs)
            Single.just(
                database.inputsDao().updateInputs(
                    inputsEntities
                )
            )
        }

    fun deleteInput(inputId: String) =
        database.inputsDao().deleteInputById(inputId)

    fun getInputById(inputId: String): Single<Input> =
        database.inputsDao().getInputById(inputId).map(::fromInputEntityToInput)
}