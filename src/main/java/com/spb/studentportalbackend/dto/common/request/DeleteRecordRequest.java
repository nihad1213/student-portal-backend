package com.spb.studentportalbackend.dto.common.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeleteRecordRequest {
    @NotNull
    Long id;
}
