package model;
import lombok.*;
import java.sql.Timestamp;
/**
 * @author:yxl
 **/
@Setter
@Getter
@ToString
public class Video {
    private int v_id;
    private Timestamp v_create_time;
    private String spec;
    private int v_user_id;
    private String v_url;
    private String v_remark;
}
