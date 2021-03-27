package com.elyasi.assignments.widgets.repository.db;

import com.elyasi.assignments.widgets.constant.GlobalConstant;
import com.elyasi.assignments.widgets.domain.Widget;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Created by Ghasem on 27/03/2021
 */
@Profile(GlobalConstant.PROFILE_H2)
@Repository
public interface H2Repository extends CrudRepository<Widget, UUID> {

    @Query("select max(z) from Widget")
    int findMaxZ();



    boolean existsWidgetByZ(int z);



    List<Widget> findAllByZGreaterThanEqualOrderByZAsc(int z);




    @Query("select w from Widget w order by w.z")
    Page<Widget> findAllWithPagination(Pageable pageable);





    @Query(value = "from Widget widget " +
            "where " +
            "widget.x >= :x1 and widget.y >= :y1 and " +
            "widget.x + widget.width <= :x2 and widget.y + widget.height <= :y2 " +
            "order by widget.z")
    Page<Widget> findAllInAreaWithPagination(Pageable pageable,
                                             @Param("x1") int x1,
                                             @Param("y1") int y1,
                                             @Param("x2") int x2,
                                             @Param("y2") int y2);

}
