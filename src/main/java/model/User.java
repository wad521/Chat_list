package model;

import java.util.Date;
import java.sql.Timestamp;
import lombok.*;
/**
 * @author:yxl
 **/

@Getter
@Setter
@ToString
public class User {
    private int id;
    private String user_name;
    private String gender;
    private String acatar_url;
    private String pwd;
    private int user_state;
    private int contact_id;
    private Timestamp create_time;
    private Date lastLogout;
    private String user_remark;
}
