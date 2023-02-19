package com.julo.weatherdex.data.favorites.implementation.repository

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.julo.weatherdex.core.extension.*
import com.julo.weatherdex.core.unit_test.CoroutineTestRule
import com.julo.weatherdex.data.city.api.model.City
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class FavoritesRepositoryImplTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var mockkRule = MockKRule(this)

    private lateinit var repository: FavoritesRepositoryImpl

    @MockK
    lateinit var gson: Gson

    @MockK
    lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val key = "FAVORITE_CITIES_KEY"
    }

    @Before
    fun setup() {
        repository = FavoritesRepositoryImpl(
            sharedPreferences = sharedPreferences,
            gson = gson,
            ioDispatcher = coroutineTestRule.testDispatcher,
        )
    }

    /** Test Cases **/

    @Test
    fun `test favoriteCity`() = runTest {
        /** Setup **/
        val city = City()

        val editor = mockk<Editor>()
        every { sharedPreferences.edit() } returns editor

        val jsonResult = randomString()
        val slotJsonParam = slot<City>()
        every { gson.toJson(capture(slotJsonParam)) } returns jsonResult

        val slotEditorKey = slot<String>()
        val slotEditorValue = slot<String>()
        every { editor.putString(capture(slotEditorKey), capture(slotEditorValue)) } returns editor

        val randomResult = randomBoolean()
        every { editor.commit() } returns randomResult

        /** Act **/
        val result = repository.favoriteCity(city)

        /** Assert **/
        randomResult assertEqual result
        key assertEqual slotEditorKey.captured
        jsonResult assertEqual slotEditorValue.captured
        city assertEqual slotJsonParam.captured
    }

    @Test
    fun `test favoriteCity exception thrown`() = runTest {
        /** Setup **/
        val city = City()

        every { sharedPreferences.edit() } throws IllegalStateException(randomString())

        /** Act **/
        val result = repository.favoriteCity(city)

        /** Assert **/
        result.assertBoolean(false)
    }

    @Test
    fun `test unfavoriteCity`() = runTest {
        /** Setup **/
        val city = City()

        val editor = mockk<Editor>()
        every { sharedPreferences.edit() } returns editor

        val slotEditorRemoveParam = slot<String>()
        every { editor.remove(capture(slotEditorRemoveParam)) } returns editor

        val randomResult = randomBoolean()
        every { editor.commit() } returns randomResult

        /** Act **/
        val result = repository.unfavoriteCity(city)

        /** Assert **/
        randomResult assertEqual result
        key assertEqual slotEditorRemoveParam.captured
    }

    @Test
    fun `test unfavoriteCity exception thrown`() = runTest {
        /** Setup **/
        val city = City()

        every { sharedPreferences.edit() } throws IllegalStateException(randomString())

        /** Act **/
        val result = repository.unfavoriteCity(city)

        /** Assert **/
        result.assertBoolean(false)
    }

    @Test
    fun `test isCityFavorited with same name`() = runTest {
        /** Setup **/
        val randomName = randomString()
        val city = City(
            name = randomName,
        )

        val slotKeyParam = slot<String>()
        every { sharedPreferences.getString(capture(slotKeyParam), any()) } returns randomString()

        every { gson.fromJson(any<String>(), any<Class<Any>>()) } returns City(
            name = randomName,
        )

        /** Act **/
        val result = repository.isCityFavorited(city)

        /** Assert **/
        key assertEqual slotKeyParam.captured
        result.assertBoolean()
    }

    @Test
    fun `test isCityFavorited with different name`() = runTest {
        /** Setup **/
        val randomName = randomString()
        val city = City(
            name = randomName,
        )

        every { sharedPreferences.getString(any(), any()) } returns randomString()

        every { gson.fromJson(any<String>(), any<Class<Any>>()) } returns City(
            name = randomString(),
        )

        /** Act **/
        val result = repository.isCityFavorited(city)

        /** Assert **/
        result.assertBoolean(false)
    }

    @Test
    fun `test isCityFavorited with empty cache`() = runTest {
        /** Setup **/
        every { sharedPreferences.getString(any(), any()) } returns ""

        /** Act **/
        val result = repository.isCityFavorited(City())

        /** Assert **/
        result.assertBoolean(false)
    }

    @Test
    fun `test isCityFavorited exception thrown`() = runTest {
        /** Setup **/
        every { sharedPreferences.getString(any(), any()) } throws IllegalStateException(randomString())

        /** Act **/
        val result = repository.isCityFavorited(City())

        /** Assert **/
        result.assertBoolean(false)
    }

    @Test
    fun `test fetchFavoriteCities`() = runTest {
        /** Setup **/
        val cityFromJson = City(
            name = randomString(),
        )

        every { sharedPreferences.getString(any(), any()) } returns randomString()
        every { gson.fromJson(any<String>(), any<Class<Any>>()) } returns cityFromJson

        /** Act **/
        val result = repository.fetchFavoriteCities()

        /** Assert **/
        (cityFromJson in result).assertBoolean()
    }

    @Test
    fun `test fetchFavoriteCities empty cache`() = runTest {
        /** Setup **/
        every { sharedPreferences.getString(any(), any()) } returns ""

        /** Act **/
        val result = repository.fetchFavoriteCities()

        /** Assert **/
        emptyList<City>() assertListEqualToIgnoreOrder result
    }

    @Test
    fun `test fetchFavoriteCities exception thrown`() = runTest {
        /** Setup **/
        every { sharedPreferences.getString(any(), any()) } throws IllegalStateException(randomString())

        /** Act **/
        val result = repository.fetchFavoriteCities()

        /** Assert **/
        emptyList<City>() assertListEqualToIgnoreOrder result
    }
}
