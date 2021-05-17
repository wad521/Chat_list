package model;

import lombok.*;
/**
 * @author:yxl
 **/
@Setter
@Getter
@ToString
public class Contact {
    private int c_id;
    private String c_type;
    private String c_imformation;
    private String c_remark;
}
