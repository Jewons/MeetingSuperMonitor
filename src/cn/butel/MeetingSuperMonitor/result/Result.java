package cn.butel.MeetingSuperMonitor.result;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import cn.butel.MeetingSuperMonitor.entity.ServiceServerInfo;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "root")
@XmlSeeAlso(
{ PagedItemsResult.class,ServiceServerInfo.class})
public class Result<T>
{

	public Result(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	time                           = df.format(new Date()).toString();
	}
    @XmlElement
    private int result = 0;

    @XmlElement
    @JsonSerialize(include = Inclusion.NON_NULL)
    @JsonProperty(value = "ret_info")
    private String info;
    
    @XmlElement
    @JsonSerialize(include = Inclusion.NON_NULL)
    @JsonProperty(value = "time")
    private String time;
    
    public int getResult()
    {
        return result;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public String toString()
    {
        return "[result:" + result + ",info:" + info +  ",time:" + time +".]";
    }

}