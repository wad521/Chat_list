package model;


import lombok.*;
import java.sql.Timestamp;
@Getter
@Setter
@ToString
/**
 * @author:yxl
 **/
public class Tlike {
    private int tlike_id;
    private Timestamp tlike_create_time;
    private int tlike_user_id;
    private int tlike_v_id;
    private String tlike_remark;
}

