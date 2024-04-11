package org.zerock.w2.service;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.zerock.w2.dao.MemberDao;
import org.zerock.w2.domain.MemberVo;
import org.zerock.w2.dto.MemberDto;
import org.zerock.w2.util.MapperUtil;

@Log4j2
public enum MemberService {
    INSTANCE;

    private MemberDao dao;
    private ModelMapper modelMapper;

    MemberService() {
        dao = new MemberDao();
        modelMapper = MapperUtil.INSTANCE.get();
    }

    public MemberDto login(String mid, String mpw) throws Exception {
        MemberVo vo = dao.getWithPassword(mid, mpw);
        MemberDto memberDto = modelMapper.map(vo, MemberDto.class);

        return memberDto;
    }
}
