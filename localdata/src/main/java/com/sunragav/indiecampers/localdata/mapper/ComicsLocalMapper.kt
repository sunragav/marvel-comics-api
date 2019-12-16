package com.sunragav.indiecampers.localdata.mapper

import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.localdata.models.ComicsLocal
import com.sunragav.indiecampers.localdata.models.Favorites
import com.sunragav.indiecampers.utils.Mapper

class ComicsLocalMapper : Mapper<ComicsEntity, ComicsLocal> {
    override fun from(model: ComicsLocal): ComicsEntity {
        return ComicsEntity(
            id = model.id,
            title = model.title,
            description = model.description,
            thumbNail = model.thumbNail,
            imageUrls = model.imageUrls,
            flagged = model.flagged
        )
    }

    override fun to(entity: ComicsEntity): ComicsLocal {
        return ComicsLocal(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            thumbNail = entity.thumbNail,
            imageUrls = entity.imageUrls,
            flagged = entity.flagged
        )
    }

}

class ComicsFavoritesMapper : Mapper<ComicsEntity, Favorites> {
    override fun from(model: Favorites): ComicsEntity {
        return ComicsEntity(
            id = model.id,
            title = model.title,
            description = model.description,
            thumbNail = model.thumbNail,
            imageUrls = model.imageUrls,
            flagged = model.flagged
        )
    }

    override fun to(entity: ComicsEntity): Favorites {
        return Favorites(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            thumbNail = entity.thumbNail,
            imageUrls = entity.imageUrls,
            flagged = entity.flagged
        )
    }
}