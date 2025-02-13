package com.stergion.githubbackend.infrastructure.persistence.mongo.utils.mapper;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface ObjectIdMapper {
    default ObjectId toObjectId(String id) {
        return new ObjectId(id);
    }

    default List<ObjectId> toObjectId(List<String> id) {
        return id.stream()
                 .map(ObjectId::new)
                 .toList();
    }

    default String toStringId(ObjectId id) {
        return id.toHexString();
    }

    default List<String> toStringId(List<ObjectId> id) {
        return id.stream()
                 .map(ObjectId::toHexString)
                 .toList();
    }
}
