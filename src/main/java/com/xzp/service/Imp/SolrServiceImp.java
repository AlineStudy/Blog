package com.xzp.service.Imp;

import com.xzp.model.UserContent;
import com.xzp.service.SolrService;
import com.xzp.service.UserContentService;
import com.xzp.utils.PageHelper;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SolrServiceImp implements SolrService {

    @Autowired
    private HttpSolrClient solrClient;  //第三方Solr服务器对象，是一个搜索引擎服务器

    @Autowired
    private UserContentService userContentService;


    /**
     * 1.新建 SolrQuery 对象，然后设置查询条件，title 为要查询的字段，keyword 为要查询的字段值。
     * 2.高亮显示必须设置 highlight 属性为 true
     * 3.设置高亮的字段 tilte
     * 4.设置高亮的标签头，可看出 style 的 color 属性值为 red，即红色高亮显示，也可自定义别的颜色。
     * 5.设置高亮的标签尾。
     * 6.分页，设置开始索引和每页显示记录数，按照时间倒序。
     * 7.Solr 客户端对象根据查询条件查询 Solr 索引库，将查询结果放入 QueryResponse 对象中。
     * 8.下面就是根据 QueryResponse 对象获取高亮结果集和所有数据结果集，然后遍历所有数据结果集，
     *     获取 Solr 索引库中的数据，将数据设置到文章对象中；然后遍历高亮结果集，主要是 title 属性，将 title 设置到文章对象中，
     *     每遍历一次将结果添加到 list 集合，这样 list 集合就包含了所有的查询结果。
     *
     * 9.用 PageHelper 分页工具开始分页，将 list 集合和总记录数等设置到 Page 中，最后将 page 对象返回
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageHelper.Page<UserContent> findByKeyWords(String keyword, Integer pageNum, Integer pageSize) {
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        solrQuery.setQuery("title:"+keyword);
        //设置高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("title");
        solrQuery.setHighlightSimplePre( "<span style='color:red'>" );
        solrQuery.setHighlightSimplePost( "</span>" );

        //分页
        if (pageNum == null || pageNum < 1)
            pageNum = 1;
        if (pageSize == null || pageSize < 1)
            pageSize = 7;

        solrQuery.setStart( (pageNum-1)*pageSize );
        solrQuery.setRows( pageSize );
        solrQuery.addSort("rpt_time", SolrQuery.ORDER.desc);//时间降序
        //开始查询

        try{
            QueryResponse response = solrClient.query(solrQuery); //获得Http的响应对象
            //获得高亮数据集合
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            //获得所有结果集
            SolrDocumentList resultList = response.getResults();
            //获得总数量
            long totalNum  = resultList.getNumFound();
            List<UserContent> list = new ArrayList<>();

            //所有结果集遍历
            for (SolrDocument solrDocument : resultList){
                //创建文本对象
                UserContent content = new UserContent();
                //文章Id
                String id = (String) solrDocument.get("id");
                Object content1 = solrDocument.get( "content" );
                Object commentNum = solrDocument.get( "comment_num" );
                Object downvote = solrDocument.get( "downvote" );
                Object upvote = solrDocument.get( "upvote" );
                Object nickName = solrDocument.get( "nick_name" );
                Object imgUrl = solrDocument.get( "img_url" );
                Object userid = solrDocument.get( "user_id" );
                Object rpt_time = solrDocument.get( "rpt_time" );
                Object category = solrDocument.get( "category" );
                Object personal = solrDocument.get( "personal" );




                //取得高亮数据集合中的文章标题
                Map<String, List<String>> map = highlighting.get(id);
                String title = map.get("title").get( 0 );

                content.setId(Long.parseLong(id));
                content.setCommentNum(Integer.parseInt(commentNum.toString()));
                content.setDownvote( Integer.parseInt( downvote.toString() ) );
                content.setUpvote( Integer.parseInt( upvote.toString() ) );
                content.setNickName( nickName.toString() );
                content.setImgUrl( imgUrl.toString() );


                if (userid == null){
                    UserContent userContent = userContentService.findById(Long.parseLong(id));
                    userid = userContent.getUserId().toString();
                }

                content.setUserId( Long.parseLong( userid.toString() ) );
                content.setTitle( title );
                content.setPersonal( personal.toString() );

                Date date = (Date) rpt_time;
                content.setRptTime(date);
                List<String> clist = (ArrayList) content1;

                content.setContent( clist.get(0).toString());
                content.setCategory( category.toString() );

                list.add( content );  //获得需要查询的结果
            }

            PageHelper.startPage(pageNum,pageSize);
            PageHelper.Page page = PageHelper.endPage();
            page.setResult(list);
            page.setTotal(totalNum);
            return page;

        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addUserContent(UserContent userContent) {
        if (userContent!=null)
            addDocument(userContent);
    }

    @Override
    public void updateUserContent(UserContent userContent) {
        if (userContent!=null)
            addDocument(userContent);
    }

    @Override
    public void deleteById(Long id) {
        try {
            solrClient.deleteById(id.toString());
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public void addDocument(UserContent cont){
        try {
            SolrInputDocument inputDocument = new SolrInputDocument();
            inputDocument.addField( "comment_num", cont.getCommentNum() );
            inputDocument.addField( "downvote", cont.getDownvote() );
            inputDocument.addField( "upvote", cont.getUpvote() );
            inputDocument.addField( "nick_name", cont.getNickName());
            inputDocument.addField( "img_url", cont.getImgUrl() );
            inputDocument.addField( "rpt_time", cont.getRptTime() );
            inputDocument.addField( "content", cont.getContent() );
            inputDocument.addField( "category", cont.getCategory());
            inputDocument.addField( "title", cont.getTitle() );
            inputDocument.addField( "u_id", cont.getUserId() );
            inputDocument.addField( "id", cont.getId());
            inputDocument.addField( "personal", cont.getPersonal());
            solrClient.add( inputDocument );
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
