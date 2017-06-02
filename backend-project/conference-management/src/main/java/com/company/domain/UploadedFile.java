package com.company.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Alex on 21.05.2017.
 */

@Table(name = "UploadedFile")
@Entity
public class UploadedFile {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "nume",unique = false, nullable = false, length = 256)
    private String nume;

    @Column(name = "filePath",unique = true, nullable = false, length = 256)
    private String filePath;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateUploaded")
    private Date dateUploaded;

    @ManyToOne()
    @JoinColumn(name = "PaperID_Abstract")
    private Paper paperIsAbstractFor;

    @ManyToOne()
    @JoinColumn(name = "PaperID_FullPaper")
    private Paper paperIsFullPaperFor;

    @OneToOne()
    @JoinColumn(name = "PaperID_Presentation")
    private Paper paperIsPresentationFor;

    public UploadedFile() {}

    public UploadedFile(String _nume, String _filePath, Date _uploadDate){
        nume=_nume;
        filePath=_filePath;
        dateUploaded=_uploadDate;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setPaperIsAbstractFor(Paper paperIsAbstractFor) {
        this.paperIsAbstractFor = paperIsAbstractFor;
    }

    public void setPaperIsFullPaperFor(Paper paperIsFullPaperFor) {
        this.paperIsFullPaperFor = paperIsFullPaperFor;
    }

    public void setPaperIsPresentationFor(Paper paperIsPresentationFor) {
        this.paperIsPresentationFor = paperIsPresentationFor;
    }

    public String getNume() {
        return nume;
    }

    public int getId() {
        return id;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public Paper getPaperIsAbstractFor() {
        return paperIsAbstractFor;
    }

    public Paper getPaperIsFullPaperFor() {
        return paperIsFullPaperFor;
    }

    public Paper getPaperIsPresentationFor() {
        return paperIsPresentationFor;
    }

    public String getFilePath() {
        return filePath;
    }
}
