package model;

import lombok.*;
import java.sql.Timestamp;
/**
 * @author:yxl
 **/
@Getter
@Setter
@ToString
public class Question {
    private int q_id;
    private String q_conntent;
    private int q_user_id;
    private int parent_q_id;
    private Timestamp q_creat_time;
    private String q_remark;

}
