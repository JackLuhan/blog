/**
 * MIT License
 * <p>
 * Copyright (c) 2018 houyi
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.houyi.blog.persistence.mapper;

import com.houyi.blog.business.vo.ArticleConditionVO;
import com.houyi.blog.persistence.beans.BizArticle;
import com.houyi.blog.plugin.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author houyi
 * @version 1.0

 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Repository
public interface BizArticleMapper extends BaseMapper<BizArticle> {

    /**
     * 分页查询，关联查询文章标签、文章类型
     *
     * @param vo
     * @return
     */
    List<BizArticle> findPageBreakByCondition(ArticleConditionVO vo);

    /**
     * 统计指定文章的标签集合
     *
     * @param list
     * @return
     */
    List<BizArticle> listTagsByArticleId(List<Long> list);

    /**
     * 获取文章详情，关联查询文章标签、文章类型
     *
     * @param id
     * @return
     */
    BizArticle get(Long id);

    /**
     * 获取上一篇和下一篇(是否可以通过get时查出来？ BizArticle中关联两个BizArticle：prev & next)
     *
     * @param insertTime
     * @return
     */
    List<BizArticle> getPrevAndNextArticles(Date insertTime);

    /**
     * 获取热门文章
     *
     * @return
     */
    List<BizArticle> listHotArticle();

    /**
     * 是否存在文章
     *
     * @param id
     * @return
     */
    Integer isExist(Long id);

    /**
     * 获取封面图片素材
     *
     * @return
     */
    List<String> listMaterial();

    /**
     * 批量修改status
     *
     * @param list
     * @param status
     * @return
     */
    int batchUpdateStatus(@Param("list") List<Long> list, @Param("status") boolean status);

}
