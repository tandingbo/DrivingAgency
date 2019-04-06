package com.beautifulsoup.driving.service.impl;

import com.beautifulsoup.driving.common.DrivingConstant;
import com.beautifulsoup.driving.common.FastDfsFile;
import com.beautifulsoup.driving.common.SecurityContextHolder;
import com.beautifulsoup.driving.dto.AgentDto;
import com.beautifulsoup.driving.dto.AnnouncementDto;
import com.beautifulsoup.driving.dto.CommentDto;
import com.beautifulsoup.driving.enums.AgentStatus;
import com.beautifulsoup.driving.enums.RoleCode;
import com.beautifulsoup.driving.exception.AuthenticationException;
import com.beautifulsoup.driving.exception.ParamException;
import com.beautifulsoup.driving.pojo.*;
import com.beautifulsoup.driving.repository.*;
import com.beautifulsoup.driving.service.AgentManageService;
import com.beautifulsoup.driving.utils.*;
import com.beautifulsoup.driving.vo.*;
import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.management.relation.RoleStatus;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgentManageServiceImpl implements AgentManageService {

    private static final Splitter splitter=Splitter.on(":").trimResults().omitEmptyStrings();

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public AgentBaseInfoVo addNewAgent(AgentDto agentDto, BindingResult result) {
        ParamValidatorUtil.validateBindingResult(result);
        Agent authentication=SecurityContextHolder.getAgent();
        if (authentication.getStatus().equals(AgentStatus.UNEXAMINED.getCode())){
            throw new AuthenticationException("对不起,你还没通过超管审核,还不能添加代理");
        }
        Agent agent=new Agent();
        BeanUtils.copyProperties(agentDto,agent);
        agent.setAgentIdcardImg(MoreObjects.firstNonNull(Strings.emptyToNull(agent.getAgentIdcardImg()),"http://www.aa.jpg"));
        agent.setAgentSchool(MoreObjects.firstNonNull(Strings.emptyToNull(agent.getAgentSchool()),"sdnu"));
        agent.setAgentAchieve(0);
        agent.setAgentPassword(MD5Util.MD5Encode("000000"));

        Agent agentByAgentName = agentRepository.findAgentByAgentName(agentDto.getAgentName());
        if (agentByAgentName != null) {
            throw new ParamException("代理的名字已经存在,添加失败");
        }


        if (authentication != null) {
            if (authentication.getParentId().equals(RoleCode.ROLE_ADMIN.getType())){
                agent.setStatus(AgentStatus.EXAMINED.getCode());
                agent.setParentId(RoleCode.ROLE_FIRST_TIER_AGENT.getType());

                Role role = roleRepository.findById(2).get();
                agent.setRole(role);
            }else{
                agent.setStatus(AgentStatus.UNEXAMINED.getCode());
                agent.setParentId(authentication.getId());
                //总业绩和当天业绩的更新
                stringRedisTemplate.opsForHash().increment(DrivingConstant.Redis.ACHIEVEMENT_DAILY,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName(),1);
                stringRedisTemplate.opsForHash().increment(DrivingConstant.Redis.ACHIEVEMENT_TOTAL,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName(),1);
                //排行榜数据的维护
                stringRedisTemplate.opsForZSet().add(DrivingConstant.Redis.ACHIEVEMENT_TOTAL_ORDER,
                        DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName(),
                        Double.parseDouble(MoreObjects.firstNonNull(Strings.emptyToNull((String) stringRedisTemplate.opsForHash()
                                .get(DrivingConstant.Redis.ACHIEVEMENT_TOTAL,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName())),"0")));
                stringRedisTemplate.opsForZSet().add(DrivingConstant.Redis.ACHIEVEMENT_DAILY_ORDER,DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName()
                ,Double.parseDouble(MoreObjects.firstNonNull(Strings.emptyToNull((String) stringRedisTemplate.opsForHash().get(DrivingConstant.Redis.ACHIEVEMENT_DAILY,
                                DrivingConstant.Redis.ACHIEVEMENT_AGENT+authentication.getAgentName())),"0")));

                Role role=roleRepository.findById(3).get();
                agent.setRole(role);

            }
            agentRepository.save(agent);
        }
        AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();
        BeanUtils.copyProperties(agent,agentBaseInfoVo);
        redisTemplate.opsForHash().put(DrivingConstant.Redis.ACHIEVEMENT_AGENTS,
                DrivingConstant.Redis.ACHIEVEMENT_AGENT+agent.getAgentName(),agentBaseInfoVo);
        return agentBaseInfoVo;
    }

    @Override
    public AnnouncementVo publishAnnouncement(AnnouncementDto announcementDto, BindingResult result) {
        ParamValidatorUtil.validateBindingResult(result);
        Announcement announcement=new Announcement();
        BeanUtils.copyProperties(announcementDto,announcement);
        announcement.setPublishTime(new Date());
        announcementRepository.save(announcement);

        AnnouncementVo announcementVo=new AnnouncementVo();
        BeanUtils.copyProperties(announcement,announcementVo);
        return announcementVo;
    }

    @Override
    public AgentBaseInfoVo examineExistsAgent(String username) {
        Agent agent=agentRepository.findAgentByAgentName(username);
        if (agent != null) {
            if (agent.getStatus().equals(AgentStatus.UNEXAMINED.getCode())){
                agent.setStatus(AgentStatus.EXAMINED.getCode());
                agentRepository.save(agent);
            }
            AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();
            BeanUtils.copyProperties(agent,agentBaseInfoVo);
            return agentBaseInfoVo;
        }
        return null;
    }

    //获取指定代理下的所有子代理,比如超管->1级代理->2级代理。如果当前用户是Admin,这里得到其下的全部一级代理和二级代理
    @Override
    public List<AgentVo> listAllAgents() {
        List<AgentVo> agentVos=Lists.newArrayList();
        Agent agent=SecurityContextHolder.getAgent();
        Set<Agent> agents=Sets.newHashSet();
        findChildrenAgents(agents,agent.getId());
        Iterator<Agent> iterator = agents.iterator();
        while (iterator.hasNext()){
            Agent next = iterator.next();
            AgentVo agentVo=new AgentVo();
            BeanUtils.copyProperties(next,agentVo);
            agentVos.add(agentVo);
        }
        List<AgentVo> collect = agentVos.stream().sorted(Comparator.comparing(AgentVo::getAgentAchieve).reversed()).collect(Collectors.toList());
        return collect;
    }

    //获取第一层子代理。比如超管->1级代理->2级代理。如果当前用户是Admin,这里只得到所有的1级代理,这种做法的原因是为了满足首页需求
    @Override
    public List<AgentVo> listAllProcessedAgents() {
        Agent agent = SecurityContextHolder.getAgent();
        ParamValidatorUtil.validateContextHolderAgent(agent);
        Set<Agent> agents= Sets.newConcurrentHashSet();
        findChildrenAgents(agents,agent.getId());
        List<AgentVo> lists= Lists.newArrayList();

        if (agent.getRole().getType().equals(RoleCode.ROLE_ADMIN.getType())){
            List<Agent> collect = agents.stream().filter(agent2 -> !agent2.getParentId().equals(1)).collect(Collectors.toList());
            collect.forEach(col->{
                agents.remove(col);
            });
        }else{
            List<Agent> collect = agents.stream().filter(agent2 -> !agent2.getParentId().equals(agent.getId())).collect(Collectors.toList());
            agents.remove(collect.get(0));
        }
        agents.stream().sorted(Comparator.comparing(Agent::getAgentAchieve).reversed()).forEach(agent1->{
            AgentVo agentVo=new AgentVo();
            RoleVo roleVo=new RoleVo();
            BeanUtils.copyProperties(agent1,agentVo);
            BeanUtils.copyProperties(agent1.getRole(),roleVo);
            String  totalAchieve = (String) stringRedisTemplate.opsForHash().get(DrivingConstant.Redis.ACHIEVEMENT_TOTAL,
                    DrivingConstant.Redis.ACHIEVEMENT_AGENT + agent1.getAgentName());
            String  dailyAchieve = (String) stringRedisTemplate.opsForHash().get(DrivingConstant.Redis.ACHIEVEMENT_DAILY,
                    DrivingConstant.Redis.ACHIEVEMENT_AGENT + agent1.getAgentName());
            if (StringUtils.isBlank(dailyAchieve)){
                agentVo.setDailyAchieve(0);
            }else{
                agentVo.setDailyAchieve(Integer.parseInt(dailyAchieve));
            }
            if (StringUtils.isBlank(totalAchieve)){
                agentVo.setAgentAchieve(0);
            }else{
                agentVo.setAgentAchieve(Integer.parseInt(totalAchieve));
            }

            agentVo.setRoleVo(roleVo);
            lists.add(agentVo);
        });
        return lists;
    }



    @Override
    public List<AgentVo> listAllUnExamineAgents() {
        Agent agent = SecurityContextHolder.getAgent();
        ParamValidatorUtil.validateContextHolderAgent(agent);
        Set<Agent> agents= Sets.newConcurrentHashSet();
        findChildrenAgents(agents,agent.getId());
        List<AgentVo> lists= Lists.newArrayList();
        agents.stream().sorted(Comparator.comparing(Agent::getAgentAchieve).reversed()).forEach(agent1->{
            AgentVo agentVo=new AgentVo();
            BeanUtils.copyProperties(agent1,agentVo);
            lists.add(agentVo);
        });
        List<AgentVo> collect = lists.stream().
                filter(agentVo -> agentVo.getStatus() == 0).collect(Collectors.toList());
        return collect;
    }

    @Override
    public AnnouncementVo getLatestAnnouncement() {
        Announcement announcement = announcementRepository.findFirstByOrderByPublishTimeDesc();
        if (announcement != null) {
            AnnouncementVo announcementVo=new AnnouncementVo();
            BeanUtils.copyProperties(announcement,announcementVo);
            return announcementVo;
        }
        return null;
    }

    @Override
    public List<AgentBaseInfoVo> listAllAgentsByDailyAchievements() {
        List<AgentVo> agentVos = listAllProcessedAgents();
        List<AgentBaseInfoVo> collect =Lists.newArrayList();
        agentVos.stream().sorted(Comparator.comparing(AgentVo::getDailyAchieve).reversed())
                .forEach(agentVo -> {
                    AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();
                    BeanUtils.copyProperties(agentVo,agentBaseInfoVo);
                    collect.add(agentBaseInfoVo);
                });
//        agentVos.stream().
        return collect;
    }

    @Override
    public List<AgentBaseInfoVo> listAllAgentsByTotalAchievements() {
        List<AgentVo> agentVos = listAllProcessedAgents();
        List<AgentBaseInfoVo> collect =Lists.newArrayList();
        agentVos.stream().sorted(Comparator.comparing(AgentVo::getAgentAchieve).reversed())
                .forEach(agentVo -> {
                    AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();
                    BeanUtils.copyProperties(agentVo,agentBaseInfoVo);
                    collect.add(agentBaseInfoVo);
                });
        return collect;
    }

    @Override
    public List<AgentBaseInfoVo> listChildrenAgentsByName(String username) {
        Agent agentByAgentName = agentRepository.findAgentByAgentName(username);
        if (agentByAgentName != null) {
            List<AgentBaseInfoVo> agentBaseInfoVos=Lists.newArrayList();
            List<Agent> allByParentId = agentRepository.findAllByParentId(agentByAgentName.getId());
            allByParentId.stream().forEach(agent -> {
                AgentBaseInfoVo agentBaseInfoVo=new AgentBaseInfoVo();
                BeanUtils.copyProperties(agent,agentBaseInfoVo);
                agentBaseInfoVos.add(agentBaseInfoVo);
            });
            //人数不是很多,可以采用内存排序
            List<AgentBaseInfoVo> collect = agentBaseInfoVos.stream()
                    .sorted(Comparator.comparing(AgentBaseInfoVo::getAgentAchieve).reversed()).collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    //排行榜系统,默认返回前10条
    @Override
    public List<AgentRankingVo> rankingListbyDailyAchievements() {
        List<AgentRankingVo> agentBaseInfoVos=Lists.newArrayList();
        List<String> collect = stringRedisTemplate.opsForZSet().reverseRange(DrivingConstant.Redis.ACHIEVEMENT_DAILY_ORDER, 0, 9).stream()
                .map(key -> key.substring(key.lastIndexOf(":") + 1)).collect(Collectors.toList());
        for (String name:collect){
            String agentKey=String.join("",DrivingConstant.Redis.RANKING_AGENT,name);
            AgentRankingVo agentRankingVo =
                    (AgentRankingVo) redisTemplate.opsForHash().get(DrivingConstant.Redis.RANKING_AGENTS, agentKey);
            if (agentRankingVo != null) {
                agentBaseInfoVos.add(agentRankingVo);
            }
        }
        return agentBaseInfoVos;
    }

    @Override
    public List<AgentRankingVo> rankingListbyTotalAchievements() {
        List<AgentRankingVo> agentBaseInfoVos=Lists.newArrayList();
        List<String> collect = stringRedisTemplate.opsForZSet().reverseRange(DrivingConstant.Redis.ACHIEVEMENT_TOTAL_ORDER, 0, 9).stream()
                .map(key -> key.substring(key.lastIndexOf(":") + 1)).collect(Collectors.toList());
        for (String name:collect){
            String agentKey=String.join("",DrivingConstant.Redis.RANKING_AGENT,name);
            AgentRankingVo agentRankingVo =
                    (AgentRankingVo) redisTemplate.opsForHash().get(DrivingConstant.Redis.RANKING_AGENTS, agentKey);
            if (agentRankingVo != null) {
                agentBaseInfoVos.add(agentRankingVo);
            }
        }
        return agentBaseInfoVos;
    }

    @Override
    public AgentRankingVo starAgent(String username) {
        boolean b = redisTemplate.opsForHash().hasKey(DrivingConstant.Redis.RANKING_AGENTS, DrivingConstant.Redis.RANKING_AGENT + username).booleanValue();
        if (!b){
            throw new ParamException("当前代理不存在,点赞失败");
        }
        Long starNums = stringRedisTemplate.opsForHash().increment(DrivingConstant.Redis.AGENT_STARS, username, 1);
        AgentRankingVo agentRankingVo =
                (AgentRankingVo) redisTemplate.opsForHash().get(DrivingConstant.Redis.RANKING_AGENTS, DrivingConstant.Redis.RANKING_AGENT+ username);
        if (agentRankingVo != null) {
            agentRankingVo.setStarNums(starNums.intValue());
            redisTemplate.opsForHash().put(DrivingConstant.Redis.RANKING_AGENTS, DrivingConstant.Redis.RANKING_AGENT+ username,agentRankingVo);
            return agentRankingVo;
        }
        return null;
    }

    @Override
    public AgentRankingVo publishCommentByAgentName(CommentDto commentDto, BindingResult result) {
        ParamValidatorUtil.validateBindingResult(result);
        boolean b = redisTemplate.opsForHash().hasKey(DrivingConstant.Redis.RANKING_AGENTS, DrivingConstant.Redis.RANKING_AGENT + commentDto.getName()).booleanValue();
        if (!b){
            throw new ParamException("当前代理不存在,点赞失败");
        }
        Agent agent=SecurityContextHolder.getAgent();
        Comment comment=new Comment();
        BeanUtils.copyProperties(commentDto,comment);
        comment.setAuthor(agent.getAgentName());
        comment.setPublishTime(new Date());
        commentRepository.save(comment);
        AgentRankingVo agentRankingVo =
                (AgentRankingVo) redisTemplate.opsForHash().get(DrivingConstant.Redis.RANKING_AGENTS, DrivingConstant.Redis.RANKING_AGENT+ commentDto.getName());
        if (agentRankingVo != null) {
            if (CollectionUtils.isEmpty(agentRankingVo.getCommentVos())){
                List<CommentVo> commentVos=Lists.newArrayList();
                agentRankingVo.setCommentVos(commentVos);
            }
            CommentVo commentVo=new CommentVo();
            BeanUtils.copyProperties(comment,commentVo);
            agentRankingVo.getCommentVos().add(commentVo);
            redisTemplate.opsForHash().put(DrivingConstant.Redis.RANKING_AGENTS, DrivingConstant.Redis.RANKING_AGENT+ comment.getName(),agentRankingVo);
            return agentRankingVo;
        }
        return null;
    }

    @Override
    public List<CommentVo> rankingCommentsListByName(String username) {
        List<Comment> allByName = commentRepository.findAllByName(username);
        List<CommentVo> commentVos=Lists.newArrayList();
        for (Comment comment:allByName){
            CommentVo commentVo=new CommentVo();
            BeanUtils.copyProperties(comment,commentVo);
            commentVos.add(commentVo);
        }
        return commentVos;
    }

    @Override
    public String derivedExcel() {
        List<String> collect = listAllAgents().stream().map(AgentVo::getAgentName).collect(Collectors.toList());
        List<Student> all = studentRepository.findAllByOperatorIn(collect, Sort.by(Sort.Order.desc("studentPrice")));
        List<StudentVo> studentVos= Lists.newArrayList();


        all.forEach(student -> {
            StudentVo studentVo=new StudentVo();
            BeanUtils.copyProperties(student,studentVo);
            studentVos.add(studentVo);
        });
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("获取学员信息Excel表格");
        HSSFRow row = null;
        row = sheet.createRow(0);
        row.setHeight((short) (26.25 * 20));
        row.createCell(0).setCellValue("学员信息列表");
        CellRangeAddress rowRegion = new CellRangeAddress(0, 0, 0, 8);
        sheet.addMergedRegion(rowRegion);
        row = sheet.createRow(1);
        row.setHeight((short) (22.50 * 20));//设置行高
        row.createCell(0).setCellValue("学员Id");//为第一个单元格设值
        row.createCell(1).setCellValue("学院学号");//为第一个单元格设值
        row.createCell(2).setCellValue("学员姓名");//为第二个单元格设值
        row.createCell(3).setCellValue("学员手机号");//为第三个单元格设值
        row.createCell(4).setCellValue("学员身份证照片地址");//为第四个单元格设值
        row.createCell(5).setCellValue("学员学费");//为第五个单元格设值
        row.createCell(6).setCellValue("学员学校");//为第六个单元格设值
        row.createCell(7).setCellValue("学员添加者");//为第七个单元格设值
        row.createCell(8).setCellValue("学员");//为第八个单元格设值

        for (int i = 0; i < studentVos.size(); i++) {
            row = sheet.createRow(i + 2);
            StudentVo studentVo = studentVos.get(i);
            row.createCell(0).setCellValue(studentVo.getId());
            row.createCell(1).setCellValue(studentVo.getStudentId());
            row.createCell(2).setCellValue(studentVo.getStudentName());
            row.createCell(3).setCellValue(studentVo.getStudentPhone());
            row.createCell(4).setCellValue(studentVo.getStudentImg());
            if (studentVo.getStudentPrice()==null){
                row.createCell(5).setCellValue(0);
            }else{
                row.createCell(5).setCellValue(studentVo.getStudentPrice().doubleValue());
            }
            row.createCell(6).setCellValue(studentVo.getStudentSchool());
            row.createCell(7).setCellValue(studentVo.getOperator());
            row.createCell(8).setCellValue(DateTimeUtil.dateToStr(studentVo.getUpdateTime()));
        }
        sheet.setDefaultRowHeight((short) (16.5 * 20));
        for (int i = 0; i <= 13; i++) {
            sheet.autoSizeColumn(i);
        }
        File file=new File("C:\\agent\\Student.xls");
        try {
            wb.write(file);
            return "Excel导出成功"+file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Set<Agent> findChildrenAgents(Set<Agent> agents,Integer parentId){
        Optional<Agent> optional = agentRepository.findById(parentId);
        if (optional.isPresent()){
            agents.add(optional.get());
        }
        List<Agent> allByParentId = agentRepository.findAllByParentId(parentId);
        for (Agent agent:allByParentId){
            findChildrenAgents(agents,agent.getId());
        }
        return agents;
    }


}
