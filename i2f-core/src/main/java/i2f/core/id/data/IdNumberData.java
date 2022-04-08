package i2f.core.id.data;

import i2f.core.annotations.remark.Author;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Author("i2f")
@Data
public class IdNumberData {
    public static SimpleDateFormat dateFmt=new SimpleDateFormat("yyyyMMdd");
    public String idNumber;
    public String region;
    public String regionDesc;
    public String date;
    public Date dateDesc;
    public String year;
    public String month;
    public String day;
    public boolean isLeap=false;
    public String policy;
    public String sex;
    public String sexDesc;
    public String checkSum;
    public boolean isLegalCheckSum=false;
    public boolean isLegalId=false;
    public String illegalReason;
}
