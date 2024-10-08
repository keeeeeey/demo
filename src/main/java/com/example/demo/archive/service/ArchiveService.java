package com.example.demo.archive.service;

import com.example.demo.archive.dto.ArchiveResponse;
import com.example.demo.archive.dto.CreateArchiveRequest;
import com.example.demo.archive.repository.ArchiveRepository;
import com.example.demo.common.entity.Archive;
import com.example.demo.common.entity.ArchiveAttach;
import com.example.demo.common.extension.FileExtension;
import com.example.demo.common.storage.IStorageManager;
import com.example.demo.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@ExtensionMethod(FileExtension.class)
public class ArchiveService {
    private final IStorageManager storageManager;
    private final ArchiveRepository archiveRepository;

    @Transactional(rollbackFor = IOException.class)
    public ArchiveResponse create(User author, CreateArchiveRequest request, List<MultipartFile> attaches) throws IOException {
        if (attaches == null) {
            attaches = new ArrayList<>();
        }

        // 아카이브 생성
        var archive = Archive.builder()
                .positionX(request.getPositionX())
                .positionY(request.getPositionY())
                .address(request.getAddress())
                .name(request.getName())
                .content(request.getContent())
                .isPublic(request.isPublic())
                .author(author)
                .build();

        archive = archiveRepository.save(archive);

        // 첨부 생성
        List<ArchiveAttach> attachEntities = new ArrayList<>();
        for (byte i = 0; i < attaches.size(); i++) {
            MultipartFile o = attaches.get(i);
            var size = o.getImageSize();
            ArchiveAttach build = ArchiveAttach.builder()
                    .name(o.getOriginalFilename())
                    .path(storageManager.put(author.getId().toString(), o))
                    .width(size.getLeft())
                    .height(size.getRight())
                    .sequence(i)
                    .archive(archive)
                    .build();
            attachEntities.add(build);
        }
        archive.setArchiveAttaches(attachEntities);

        return ArchiveResponse.fromEntity(archive);
    }
}
