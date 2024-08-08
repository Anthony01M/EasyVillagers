package xyz.berrystudios.easyvillagers.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import xyz.berrystudios.easyvillagers.database.table.annotations.Column;
import xyz.berrystudios.easyvillagers.database.table.annotations.Table;

@Data
@AllArgsConstructor
@Table(name = "custom_blocks")
public class CustomBlocks {
    @Column(name = "location", primary = true)
    private String location;
    @Column(name = "type")
    private String type;
    @Column(name = "updated_on")
    private long updatedOn;
    @Column(name = "data")
    private String data;
}
