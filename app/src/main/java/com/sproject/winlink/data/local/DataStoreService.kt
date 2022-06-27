package com.sproject.winlink.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sproject.winlink.data.remote.dto.PcInfosDto
import com.sproject.winlink.data.remote.mapper.toPcInfos
import com.sproject.winlink.data.remote.mapper.toPcInfosDto
import com.sproject.winlink.domain.model.PcInfos
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private object PreferencesKeys {
    val LAST_CONNECTED_PC = stringPreferencesKey("last_connected_pc")
}

class DataStoreService(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun saveLastConnectedPc(pc: PcInfos) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_CONNECTED_PC] = Json.encodeToString(pc.toPcInfosDto())
        }
    }

    suspend fun getLastConnectedPc() =
        dataStore.data.map { preferences ->
            val pcInfos = preferences[PreferencesKeys.LAST_CONNECTED_PC]

            if (pcInfos != null) {
                Json.decodeFromString<PcInfosDto>(pcInfos).toPcInfos()
            } else {
                null
            }
        }.first()
}
