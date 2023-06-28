package com.demo.library.user.mapper;

import com.demo.library.loanNban.dto.LoanDto;
import com.demo.library.loanNban.mapper.LoanMapper;
import com.demo.library.mapper.Mapper;
import com.demo.library.user.dto.UserDto;
import com.demo.library.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final LoanMapper loanMapper;
    public final Mapper mapper;

    public User postToUser(UserDto.Post post) {
        return mapper.map(post,User.class);
    }

    public User patchToUser(UserDto.Patch patch) {
        return mapper.map(patch,User.class);
    }

    public UserDto.Response userToResponse(User user)  {
        UserDto.Response response = mapper.map(user, UserDto.Response.class);
        List<LoanDto.Response> loanResponses = loanMapper.loansToLoanResponses(user.getLoans());
        response.setLoans(loanResponses);
        return response;
    }

}