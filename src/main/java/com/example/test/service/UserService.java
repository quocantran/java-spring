package com.example.test.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Company;
import com.example.test.domain.User;
import com.example.test.domain.response.ResponseMetaDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.ResponseUserDTO;
import com.example.test.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private CompanyService companyService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
            CompanyService companyService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.companyService = companyService;
    }

    public ResponseUserDTO createUser(User user) throws BadRequestException {
        User checkUser = this.userRepository.findByEmail(user.getEmail());
        if (checkUser != null) {
            throw new BadRequestException("User already exists");
        }

        Company company = this.companyService.getCompanyById(user.getCompany().getId());

        if (company == null) {
            throw new BadRequestException("Company not found");
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User res = this.userRepository.save(user);

        return res.convertUserDto();
    }

    public void deleteUser(long id) throws BadRequestException {
        if (id < 0 || id > 2999) {
            throw new BadRequestException("Invalid user id");
        }
        User user = this.userRepository.findById(id);
        if (user == null) {
            throw new BadRequestException("User not found");
        }

        this.userRepository.deleteById(id);
    }

    public ResponsePaginationDTO getAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> user = this.userRepository.findAll(spec, pageable);

        ResponsePaginationDTO resultPaginationDTO = new ResponsePaginationDTO();

        ResponseMetaDTO meta = new ResponseMetaDTO();

        meta.setPage(user.getNumber() + 1);
        meta.setPageSize(user.getSize());

        meta.setPages(user.getTotalPages());
        meta.setTotal(user.getTotalElements());

        resultPaginationDTO.setMeta(meta);

        Page<ResponseUserDTO> userDTO = user.map(User::convertUserDto);
        resultPaginationDTO.setResult(userDTO.getContent());
        return resultPaginationDTO;
    }

    public ResponseUserDTO getUserById(long id) throws BadRequestException {
        if (id < 0 || id > 2999) {
            throw new BadRequestException("Invalid user id");
        }
        User user = this.userRepository.findById(id);
        if (user == null) {
            throw new BadRequestException("User not found");
        }
        return user.convertUserDto();
    }

    public User getUserByUsername(String username) {

        return this.userRepository.findByEmail(username);
    }

    public void updateUserToken(String email, String refreshToken) {
        User currUser = this.getUserByUsername(email);
        if (currUser != null) {
            currUser.setRefreshToken(refreshToken);
            this.userRepository.save(currUser);
        }
    }
}
