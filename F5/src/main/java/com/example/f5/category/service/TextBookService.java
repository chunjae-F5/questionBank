package com.example.f5.category.service;

import com.example.f5.category.dto.TextBookDto;
import com.example.f5.category.entity.TextBook;
import com.example.f5.category.repository.TextBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TextBookService {

    private final TextBookRepository tbRepository;

    public List<TextBookDto.ResponseDto> getTextBookInfo() {
        List<TextBook> textBooks = tbRepository.findAll();
        List<TextBookDto.ResponseDto> items = new ArrayList<>();

        for (TextBook textBook : textBooks) {
            TextBookDto.ResponseDto item = new TextBookDto.ResponseDto(textBook.getItemId(), textBook.getImgUrl(), textBook.getName(), textBook.getWriter(), textBook.getCurriculum());
            items.add(item);
        }
        return items;
    }
}
