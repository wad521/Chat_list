package model;
import lombok.*;
import java.sql.Timestamp;
/**
 * @author:yxl
 **/
@Setter
@Getter
@ToString
public class Comment {
    private int com_id;
    private Timestamp com_create_time;
    private String com_content;
    private int com_v_id;
    private int com_user_id;
    private int parent_com_id;
    private String com_remark;
}
