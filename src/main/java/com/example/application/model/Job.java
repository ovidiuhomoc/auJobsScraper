package com.example.application.model;

import com.example.application.model.attributeConvertors.JSONObjectConvertor;
import com.example.application.model.attributeConvertors.JobPlatformOriginConvertor;
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
    private Long advertiserId;
    @Column
    private String area;
    @Column
    private Long areaId;
    @Column
    private String location;
    @Column
    private Long locationId;
    @Column
    private String logoLink;
    @Column
    private String company;
    @Column
    private Long companyId;
    @Column
    private String jobClassification;
    @Column
    private Long jobClassificationId;
    @Column
    private boolean isPremium = false;
    @NotNull
    @Column(nullable = false)
    private Long jobId;
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
    private Long jobSubClassificationId;
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
        try {
            JSONObject advertiser = jsonJob.has("advertiser") ? jsonJob.getJSONObject("advertiser") : null;
            advertiserName = advertiser != null ? advertiser.getString("description") : "";
            advertiserId = advertiser != null ? advertiser.getLong("id") : 0;
        } catch (Exception e) {
            long tempId = jsonJob.has("id") ? jsonJob.getLong("id") : 0;
            System.out.println("Error extracting advertiser data for job id: " + tempId + " " + e.getMessage());
        }
    }

    private void extractAreaAndLocationData() {
        try {
            area = jsonJob.has("area") ? jsonJob.getString("area") : "";
            areaId = jsonJob.has("areaId") ? jsonJob.getLong("areaId") : 0;
            location = jsonJob.has("location") ? jsonJob.getString("location") : "";
            locationId = jsonJob.has("locationId") ? jsonJob.getLong("locationId") : 0;
        } catch (Exception e) {
            long tempId = jsonJob.has("id") ? jsonJob.getLong("id") : 0;
            System.out.println("Error extracting area and location data for job id: " + tempId + " " + e.getMessage());
        }
    }

    private void extractCompanyData() {
        try {
            JSONObject logo = jsonJob.has("branding") ? jsonJob.getJSONObject("branding").getJSONObject("assets").getJSONObject("logo") : null;
            logoLink = logo != null ? logo.getJSONObject("strategies").getString("jdpLogo") : "";
            company = jsonJob.has("companyName") ? jsonJob.getString("companyName") : jsonJob.has("advertiser") ? jsonJob.getJSONObject("advertiser").getString("description") : "";
            companyId = jsonJob.has("companyProfileStructuredDataId") ? jsonJob.getLong("companyProfileStructuredDataId") : 0;
        } catch (Exception e) {
            long tempId = jsonJob.has("id") ? jsonJob.getLong("id") : 0;
            System.out.println("Error extracting company data for job id: " + tempId + " " + e.getMessage());
        }
    }

    private void extractJobDetails() {
        try {
            jobClassification = jsonJob.has("classification") ? jsonJob.getJSONObject("classification").getString("description") : "";
            jobClassificationId = jsonJob.has("classification") ? jsonJob.getJSONObject("classification").getLong("id") : 0;
            isPremium = jsonJob.has("isPremium") && jsonJob.getBoolean("isPremium");
            jobSubClassification = jsonJob.has("subClassification") ? jsonJob.getJSONObject("subClassification").getString("description") : "";
            jobSubClassificationId = jsonJob.has("subClassification") ? jsonJob.getJSONObject("subClassification").getLong("id") : 0;
            jobId = jsonJob.has("id") ? jsonJob.getLong("id") : 0;

            JSONArray jsonBulletPointsArray = jsonJob.has("bulletPoints") ? jsonJob.getJSONArray("bulletPoints") : null;
            if (jsonBulletPointsArray != null) {
                for (int i = 0; i < jsonBulletPointsArray.length(); i++) {
                    jobCardBulletPoints.add(jsonBulletPointsArray.getString(i));
                }
            }

            ZonedDateTime zonedDT = jsonJob.has("listingDate") ? ZonedDateTime.parse(jsonJob.getString("listingDate"), DateTimeFormatter.ISO_ZONED_DATE_TIME) : null;
            listingDateTime = zonedDT != null ? zonedDT.toLocalDateTime() : null;

            salary = jsonJob.has("salary") ? jsonJob.getString("salary") : "";
            teaser = jsonJob.has("teaser") ? jsonJob.getString("teaser") : "";
            title = jsonJob.has("title") ? jsonJob.getString("title") : "";
            workType = jsonJob.has("workType") ? jsonJob.getString("workType") : "";
        } catch (Exception e) {
            long tempId = jsonJob.has("id") ? jsonJob.getLong("id") : 0;
            System.out.println("Error extracting job details data for job id: " + tempId + " " + e.getMessage());
        }
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

    public String toShortString() {
        return "Job{" +
                "id=" + id +
                ", jobId=" + jobId +
                ", title='" + title + '\'' +
                ", company='" + company + '\'' +
                ", location='" + location + '\'' +
                ", area='" + area + '\'' +
                ", platformOrigin=" + platformOrigin + '\'' +
                ", classification=" + jobClassification + '\'' +
                ", subClassification=" + jobSubClassification + '\'' +
                ", salary=" + salary + '\'' +
                ", workType=" + workType + '\'' +
                ", listingDateTime=" + listingDateTime + '\'' +
                ", teaser='" + teaser + '\'' +
                ", bulletPoints=" + jobCardBulletPoints +
                '}';
    }
}
