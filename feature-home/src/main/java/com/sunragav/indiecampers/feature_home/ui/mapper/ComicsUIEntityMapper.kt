package com.sunragav.indiecampers.feature_home.ui.mapper

import com.sunragav.indiecampers.feature_home.ui.models.ComicsUIModel
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.utils.Mapper

class ComicsUIEntityMapper : Mapper<ComicsEntity, ComicsUIModel> {
    override fun from(model: ComicsUIModel): ComicsEntity {
        return ComicsEntity(
            id = model.id,
            title = model.title,
            description = model.description,
            thumbNail = model.thumbNail,
            imageUrls = model.imageUrls,
            flagged = model.flagged
        )
    }

    override fun to(entity: ComicsEntity): ComicsUIModel {
        return ComicsUIModel(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            thumbNail = entity.thumbNail,
            imageUrls = entity.imageUrls,
            flagged = entity.flagged
        )
    }

}