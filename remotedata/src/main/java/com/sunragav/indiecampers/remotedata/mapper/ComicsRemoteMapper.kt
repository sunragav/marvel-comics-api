package com.sunragav.indiecampers.remotedata.mapper

import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.remotedata.models.Comic
import com.sunragav.indiecampers.remotedata.models.Image
import com.sunragav.indiecampers.utils.Mapper

class ComicsRemoteMapper : Mapper<ComicsEntity, Comic> {
    override fun from(model: Comic): ComicsEntity {
        return ComicsEntity(
            id = model.id.toString(),
            title = model.title,
            description = model.description ?: "Sorry!! Description is coming very soon!!",
            thumbNail = with(model.thumbnail) { "$path.$extension" },
            imageUrls = model.images.map { "${it.path}.${it.extension}" },
            flagged = false
        )
    }

    override fun to(entity: ComicsEntity): Comic {
        return Comic(
            id = entity.id.toInt(),
            title = entity.title,
            description = entity.description,
            thumbnail = getImage(entity.thumbNail),
            images = entity.imageUrls.map { getImage(it) }
        )
    }

    private fun getImage(str: String): Image {
        val (path, ext) = str.split(".")
        return Image(path = path, extension = ext)
    }
}