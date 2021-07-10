package com.Abdelwahab.Live.With.ME.repositories;

import com.Abdelwahab.Live.With.ME.entities.Article;
import com.Abdelwahab.Live.With.ME.entities.Client;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Optional;


@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    @Query(value = "SELECT a FROM Article a , Deal d WHERE d.article=a AND a.isActive=true AND d.open=true AND UPPER(a.title) LIKE :title")
    List<Article> findByTitleLikeIgnoreCase(String title);
    @Query(value = "SELECT a FROM Article a , Deal d WHERE d.article=a AND a.isActive=true AND d.open=true AND UPPER(a.country) LIKE :country")
    List<Article> findByCountryLikeIgnoreCase(String country);
    @Query(value = "SELECT a FROM Article a , Deal d WHERE d.article=a AND a.isActive=true AND d.open=true AND UPPER(a.state) LIKE :state")
    List<Article> findByStateLikeIgnoreCase(String state);
    @Query(value = "SELECT a FROM Article a , Deal d WHERE d.article=a AND a.isActive=true AND d.open=true AND UPPER(a.province) LIKE :province")
    List<Article> findByProvinceLikeIgnoreCase(String province);
    @Query(value = "SELECT a FROM Article a , Deal d WHERE d.article=a AND a.isActive=true AND d.open=true AND UPPER(a.description) LIKE :description")
    List<Article> findByDescriptionLikeIgnoreCase(String description);
    @Query(value = "SELECT a FROM Article a , Deal d WHERE d.article=a AND a.isActive=true AND d.open=true AND UPPER(a.type) LIKE :type")
    List<Article> findByTypeLikeIgnoreCase(String type);
    List<Article> findByPrice(int price);

    @Override
//    @Modifying
    @Query(value = "SELECT a FROM Article a , Deal d WHERE d.article=a AND a.isActive=true AND d.open=true ")
    List<Article> findAll();
    @Query(value = "SELECT a FROM Article a , Deal d WHERE d.article=a AND a.isActive=true AND d.open=false ")
    List<Article> findAllClosed();
    @Query(value = "SELECT a FROM Article a WHERE a.isActive=false")
    List<Article> findAllDeleted();



    @Query(value = "SELECT a FROM Article a , Deal d WHERE d.article=a AND a.client=:client AND a.isActive=true AND d.open=true")
    List<Article> findByClient(Client client);
    @Query(value = "SELECT a FROM Article a , Deal d WHERE d.article=a AND a.client=:client AND a.isActive=true AND d.open=false ")
    List<Article> findClosedByClient(Client client);

//    @Modifying
//    @Query(value = "SELECT a FROM Article a , Deal d WHERE d.article=a AND a.id=:aLong AND a.isActive=true AND d.open=true ")
//    Optional<Article> getOneByID(Long aLong);

//    @Override
//    <S extends Article> Optional<S> findOne(Example<S> example);

//    @Override
    @Query(value = "SELECT a FROM Article a , Deal d WHERE d.article=a AND a.id=:aLong AND a.isActive=true AND d.open=true ")
    Article getOneById(Long aLong);


    //    @Modifying
//    @Query(value="UPDATE Article x set x=:y where x.id= :id")
//    void updateArticle(@Param(value = "id") Long id, Article y);
//    @Modifying
//    @Query(value="UPDATE Article x set x.title=:title where x.id= :id")
//    void updateArticleTitle(@Param(value = "id") Long id,@Param(value = "title") String title);
//    @Modifying
//    @Query(value="UPDATE Article x set x.country=:country where x.id= :id")
//    void updateArticleCountry(@Param(value = "id") Long id,@Param(value = "country") String country);
//    @Modifying
//    @Query(value="UPDATE Article x set x.state=:state where x.id= :id")
//    void updateArticleState(@Param(value = "id") Long id,@Param(value = "state") String state);
//    @Modifying
//    @Query(value="UPDATE Article x set x.province=:province where x.id= :id")
//    void updateArticleProvince(@Param(value = "id") Long id,@Param(value = "province") String province);
//    @Modifying
//    @Query(value="UPDATE Article x set x.description=:description where x.id= :id")
//    void updateArticleDescription(@Param(value = "id") Long id, @Param(value = "description")String description);
//    @Modifying
//    @Query(value="UPDATE Article x set x.type=:type where x.id= :id")
//    void updateArticleType(@Param(value = "id") Long id,@Param(value = "type") String type);
//    @Modifying
//    @Query(value="UPDATE Article x set x.price=:price where x.id= :id")
//    void updateArticlePrice(@Param(value = "id") Long id,@Param(value = "price") int price);
//    @Modifying
//    @Query(value="UPDATE Article x set x.priceType=:type where x.id= :id")
//    void updateArticlePriceType(@Param(value = "id") Long id, String type);
//    @Modifying
//    @Query(value="UPDATE Article x set x.numberOfRoomMates=:numberOfRoomMates where x.id= :id")
//    void updateArticleRoomMatesNumber(@Param(value = "id") Long id,@Param(value = "numberOfRoomMates") int numberOfRoomMates);
//    @Modifying
//    @Query(value="UPDATE Article x set x.remainingRoomMates=:remainingRoomMates where x.id= :id")
//    void updateArticleRemainingRoomMatesNumber(@Param(value = "id") Long id,@Param(value = "remainingRoomMates") int remainingRoomMates);






}
