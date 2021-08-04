package jp.co.internous.rainbow.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import jp.co.internous.rainbow.model.domain.MstDestination;

@Mapper
public interface MstDestinationMapper {
	
	// @Options(useGeneratedKeys=true, keyProperty="id")
	// 上記のアノテーションを使うことで自動連番されたidを
	// 取得し、domainのidに設定ことができる
	@Insert ("INSERT INTO mst_destination ("
			+ "user_id, family_name, first_name, tel_number, address"
			+ ") "
			+ "VALUES ("
			+ " #{userId}, #{familyName}, #{firstName}, #{telNumber}, #{address}"
			+ ")")
	@Options(useGeneratedKeys=true, keyProperty="id")
	int insert(MstDestination destination);
	
	@Select("SELECT * FROM mst_destination WHERE user_id = #{userId} AND status = 1 ORDER BY id ASC")
	List<MstDestination> findByUserId(@Param("userId") int userId);
	
	@Update("UPDATE mst_destination SET status = 0, updated_at = now() WHERE id = #{id}")
	int logicalDeleteById(@Param("id") int id);
}
