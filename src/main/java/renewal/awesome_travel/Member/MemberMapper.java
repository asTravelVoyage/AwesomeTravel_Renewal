package renewal.awesome_travel.Member;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {    
    @Mapping(target = "id", ignore = true)
    Member MemberDtoToMember(MemberDto memberDto);
}
