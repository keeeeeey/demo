package com.example.demo.archive.dto;

import com.example.demo.common.entity.Archive;
import com.example.demo.common.entity.ArchiveAttach;
import com.example.demo.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Builder
public class ArchiveResponse {
    private Long id;

    private User author;

    private BigDecimal positionX;

    private BigDecimal positionY;

    private String address;

    private String name;

    private String content;

    private Boolean isPublic;

    private List<AttachResponse> archiveAttaches;

    public static ArchiveResponse fromEntity(Archive archive) {
        return builder()
                .id(archive.getId())
                .positionX(archive.getPositionX())
                .positionY(archive.getPositionY())
                .address(archive.getAddress())
                .name(archive.getName())
                .content(archive.getContent())
                .isPublic(archive.getIsPublic())
                .archiveAttaches(archive.getArchiveAttaches().stream().map(AttachResponse::fromEntity).toList())
                .build();
    }
}
