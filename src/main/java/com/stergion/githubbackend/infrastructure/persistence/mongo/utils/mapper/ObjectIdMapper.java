package com.stergion.githubbackend.infrastructure.persistence.mongo.utils.mapper;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(config = MapStructConfig.class)
public interface ObjectIdMapper {
    default ObjectId toObjectId(String id) {
        return id == null ? null : new ObjectId(id);
    }

    default List<ObjectId> toObjectId(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        return ids.stream()
                  .filter(Objects::nonNull)
                  .map(ObjectId::new)
                  .toList();
    }

    default String toStringId(ObjectId id) {
        return id == null ? null : id.toHexString();
    }

    default List<String> toStringId(List<ObjectId> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        return ids.stream()
                  .filter(Objects::nonNull)
                  .map(ObjectId::toHexString)
                  .toList();
    }
}
