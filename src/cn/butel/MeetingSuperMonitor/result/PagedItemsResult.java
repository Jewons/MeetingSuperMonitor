package cn.butel.MeetingSuperMonitor.result;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;



@XmlRootElement(name = "page")
@JsonSerialize(include = Inclusion.NON_NULL)
public class PagedItemsResult<T extends Object> extends ItemsResult<T>
{

    protected Integer pageNo;

    protected Integer pageSize;

    @XmlElement(name = "totalCount")
    @JsonProperty("totalCount")
    protected long total;

    protected long totalPage;

    public Integer getPageNo()
    {
        return pageNo;
    }

    public void setPageNo(Integer pageNo)
    {
        this.pageNo = pageNo;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public long getTotalPage()
    {
        return totalPage;
    }

    public void setTotalPage(long totalPage)
    {
        this.totalPage = totalPage;
    }

}