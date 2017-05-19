package cn.butel.MeetingSuperMonitor.result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import cn.butel.MeetingSuperMonitor.entity.ServiceServerInfo;





@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "itemsResult")
@JsonSerialize(include = Inclusion.NON_EMPTY)
public class ItemsResult<T extends Object> extends Result<Object>
{

    public ItemsResult()
    {
    	
    }


    private Integer pageSize;
    private Integer currPage;
    private Integer count;
    
    private String url;
    private int index;
//    private String time;
    
    public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCurrPage() {
		return currPage;
	}

	public void setCurrPage(Integer currPage) {
		this.currPage = currPage;
	}

	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}


	@JsonSerialize(include = Inclusion.NON_NULL)
	private T data; 
    
    private Object object;
    
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    @XmlElements(value =
    {@XmlElement(type = ServiceServerInfo.class)})
    private List<T> items=new ArrayList<T>();
    
    public List<T> getItems()
    {
        return items;
    }

    public void setItems(List<T> items)
    {
      if(items != null)
        this.items = items;
    }

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	    
}