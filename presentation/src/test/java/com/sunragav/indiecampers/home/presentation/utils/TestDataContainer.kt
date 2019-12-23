package com.sunragav.indiecampers.home.presentation.utils

import com.sunragav.indiecampers.home.domain.entities.ComicsEntity


class TestDataContainer {

    companion object {
        private fun getComics(): ComicsEntity {
            return ComicsEntity(
                "143",
                "Startling Stories: The Incorrigible Hulk (2004) #1",
                "For Doctor Bruce Banner life is anything but normal. But what happens when two women get between him and his alter ego, the Incorrigible Hulk? Hulk confused! \\r\\nIndy superstar Peter Bagge (THE MEGALOMANIACAL SPIDER-MAN) takes a satirical jab at the Hulk mythos with a tale of dames, debauchery and destruction.",
                "thumbnail.jpg",
                listOf("image.jg"),
                false
            )
        }

        fun getComicsList() =
            listOf(getComics(), getComics().copy(id = "123"), getComics().copy(id = "134"))
    }
}