package com.example.application.entities.attributeConvertors;

import com.example.application.entities.JobPlatformOrigin;
import org.json.JSONObject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter
public class JobPlatformOriginConvertor implements AttributeConverter<JobPlatformOrigin, String> {
    @Override
    public String convertToDatabaseColumn(JobPlatformOrigin jobPlatformOrigin) {
        if (Objects.nonNull(jobPlatformOrigin)) return jobPlatformOrigin.toString();
        return "";
    }

    @Override
    public JobPlatformOrigin convertToEntityAttribute(String jobPlatformOrigin) {
        if (jobPlatformOrigin.isEmpty()) return null;
        return JobPlatformOrigin.valueOf(jobPlatformOrigin);
    }
}
