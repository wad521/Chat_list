package model;
import lombok.*;
/**
 * @author:yxl
 **/
@Setter
@Getter
@ToString
public class Advertisement {
    private int ad_id;
    private String ad_description;
    private String ad_url;
    private int ad_type;
    private String ad_remark;
}
