package com.sunragav.indiecampers.home.data.mappers

import com.sunragav.indiecampers.home.data.models.ComicsData
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.utils.Mapper


class ComicsDataMapper : Mapper<ComicsEntity, ComicsData> {
    override fun from(model: ComicsData): ComicsEntity {
        return ComicsEntity(
            id = model.id,
            title = model.title,
            description = model.description,
            thumbNail = model.thumbNail,
            imageUrls = model.imageUrls,
            flagged = model.flagged
        )
    }

    override fun to(entity: ComicsEntity): ComicsData {
        return ComicsData(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            thumbNail = entity.thumbNail,
            imageUrls = entity.imageUrls,
            flagged = entity.flagged
        )
    }
}