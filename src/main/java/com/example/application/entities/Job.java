package com.example.application.entities;

import com.example.application.entities.attributeConvertors.JSONObjectConvertor;
import com.example.application.entities.attributeConvertors.JobPlatformOriginConvertor;
import lombok.*;
import org.hibernate.Hibernate;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Jobs")
public class Job {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "job_card_bullet_points", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "job_card_bullet_points")
    private List<String> jobCardBulletPoints = new ArrayList<>();

    @Column(columnDefinition = "VARCHAR(25)")
    @Convert(converter = JSONObjectConvertor.class)
    private JSONObject jsonJob;

    @Column
    private String advertiserName;
    @Column
    private int advertiserId;
    @Column
    private String area;
    @Column
    private int areaId;
    @Column
    private String location;
    @Column
    private int locationId;
    @Column
    private String logoLink;
    @Column
    private String company;
    @Column
    private int companyId;
    @Column
    private String jobClassification;
    @Column
    private int jobClassificationId;
    @Column
    private boolean isPremium = false;
    @NotNull
    @Column(nullable = false)
    private int jobId;
    @Column
    private LocalDateTime listingDateTime;
    @Column
    private String salary;
    @Column
    private String teaser;
    @Column
    private String title;
    @Column
    private String jobSubClassification;
    @Column
    private int jobSubClassificationId;
    @Column
    private String workType;

    @NotNull
    @Column(nullable = false)
    @Convert(converter = JobPlatformOriginConvertor.class)
    private JobPlatformOrigin platformOrigin;

    public Job(JobPlatformOrigin platformOrigin, JSONObject jsonJob) {
        this.platformOrigin = platformOrigin;
        this.jsonJob = jsonJob;
        parseJobDetails();
    }

    public void parseJobDetails() {
        extractAdvertiserData();
        extractAreaAndLocationData();
        extractCompanyData();
        extractJobDetails();
    }

    private void extractAdvertiserData() {
        JSONObject advertiser = jsonJob.has("advertiser") ? jsonJob.getJSONObject("advertiser") : null;
        advertiserName = advertiser != null ? advertiser.getString("description") : "";
        advertiserId = advertiser != null ? advertiser.getInt("id") : 0;
    }

    private void extractAreaAndLocationData() {
        area = jsonJob.has("area") ? jsonJob.getString("area") : "";
        areaId = jsonJob.has("areaId") ? jsonJob.getInt("areaId") : 0;
        location = jsonJob.has("location") ? jsonJob.getString("location") : "";
        locationId = jsonJob.has("locationId") ? jsonJob.getInt("locationId") : 0;
    }

    private void extractCompanyData() {
        JSONObject logo = jsonJob.has("branding") ? jsonJob.getJSONObject("branding").getJSONObject("assets").getJSONObject("logo") : null;
        logoLink = logo != null ? logo.getJSONObject("strategies").getString("jdpLogo") : "";
        company = jsonJob.has("companyName") ? jsonJob.getString("companyName") : jsonJob.has("advertiser") ? jsonJob.getJSONObject("advertiser").getString("description") : "";
        companyId = jsonJob.has("companyProfileStructuredDataId") ? jsonJob.getInt("companyProfileStructuredDataId") : 0;
    }

    private void extractJobDetails() {
        jobClassification = jsonJob.has("classification") ? jsonJob.getJSONObject("classification").getString("description") : "";
        jobClassificationId = jsonJob.has("classification") ? jsonJob.getJSONObject("classification").getInt("id") : 0;
        isPremium = jsonJob.has("isPremium") && jsonJob.getBoolean("isPremium");
        jobSubClassification = jsonJob.has("subClassification") ? jsonJob.getJSONObject("subClassification").getString("description") : "";
        jobSubClassificationId = jsonJob.has("subClassification") ? jsonJob.getJSONObject("subClassification").getInt("id") : 0;
        jobId = jsonJob.has("id") ? jsonJob.getInt("id") : 0;

        JSONArray jsonBulletPointsArray = jsonJob.has("bulletPoints") ? jsonJob.getJSONArray("bulletPoints") : null;
        if (jsonBulletPointsArray != null) {
            jsonBulletPointsArray.iterator().forEachRemaining(bulletPoint -> {
                jobCardBulletPoints.add((String) bulletPoint);
            });
        }

        ZonedDateTime zonedDT = jsonJob.has("listingDate") ? ZonedDateTime.parse(jsonJob.getString("listingDate"), DateTimeFormatter.ISO_ZONED_DATE_TIME) : null;
        listingDateTime = zonedDT != null ? zonedDT.toLocalDateTime() : null;

        salary = jsonJob.has("salary") ? jsonJob.getString("salary") : "";
        teaser = jsonJob.has("teaser") ? jsonJob.getString("teaser") : "";
        title = jsonJob.has("title") ? jsonJob.getString("title") : "";
        workType = jsonJob.has("workType") ? jsonJob.getString("workType") : "";
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Job job = (Job) o;
        return id != null && Objects.equals(id, job.id);
    }
}
