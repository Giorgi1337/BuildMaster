package build.master.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "cpus")
public class CPU extends Component {
    private String socket;
    private Integer cores;
    private Integer threads;
    private Double baseClock;
    private Double boostClock;
    private Integer tdp;
    private String architecture;
    private Boolean integratedGraphics;

    @Lob
    private byte[] image;
}