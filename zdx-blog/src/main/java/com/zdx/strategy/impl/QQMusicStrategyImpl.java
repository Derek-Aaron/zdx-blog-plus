package com.zdx.strategy.impl;

import cn.hutool.core.util.ObjUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zdx.enums.MusicTypeEnum;
import com.zdx.model.vo.front.MusicVo;
import com.zdx.service.tk.ConfigService;
import com.zdx.strategy.MusicStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class QQMusicStrategyImpl implements MusicStrategy {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ConfigService configService;
    @Override
    public MusicTypeEnum musicType() {
        return MusicTypeEnum.QQ;
    }

    @Override
    public List<MusicVo> execute(String musicId) {
        List<MusicVo> musicVos = new ArrayList<>();
        String musicInfoStr = getQQMusicInfo("{\"comm\":{\"cv\":4747474,\"ct\":24,\"format\":\"json\",\"inCharset\":\"utf-8\",\"outCharset\":\"utf-8\",\"notice\":0,\"platform\":\"yqq.json\",\"needNewCode\":1,\"uin\":2488288090,\"g_tk_new_20200303\":31370172,\"g_tk\":31370172},\"req_1\":{\"module\":\"music.trackInfo.UniformRuleCtrl\",\"method\":\"CgiGetTrackInfo\",\"param\":{\"ids\":[382816367,201260252,203705374,384711659,406034797,201266855,473137,388750337,237266611,283917728,425164909,421073064,424728186,424960612,402189410,424949159,424687429,424651105,424719119,425174584,425007401,425009993,424939988,424209173,425217162,425218397,425195481,421448709,424415867,424300033,424109918,424970358,424663501,424651405,424144714,423631813,424957033,424950910,424324275,420204524,420296449,424313928,421521313,424537147,419052336,424537131,424423378,424417303,424417724,424961896,424455505,424416061,424388331,424415586,424389231,424169524,424459797,424687815,424725172,424717640,424083893,423116267,423942353,424992217,420006062,420387218,425129666,425129563,425192326,425159864,424759908,424653092,424947410,424458785,425205074,423120157,424991715,424959168,424162303,424408874,424682318,425129859,425130500,424930252,424916375,424432365,425130890,424345259,422031797],\"types\":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}},\"req_2\":{\"module\":\"userInfo.VipQueryServer\",\"method\":\"SRFVipQuery_V2\",\"param\":{\"uin_list\":[\"2488288090\"]}},\"req_3\":{\"module\":\"userInfo.BaseUserInfoServer\",\"method\":\"get_user_baseinfo_v2\",\"param\":{\"vec_uin\":[\"2488288090\"]}},\"req_4\":{\"module\":\"music.lvz.VipIconUiShowSvr\",\"method\":\"GetVipIconUiV2\",\"param\":{\"PID\":3}}}", "https://u.y.qq.com/cgi-bin/musics.fcg?_=1690709719562&sign=zzb65a12d7ait8emymgfvm1fcidb1ilpaa52f657b");
        String musicFileStr = getQQMusicInfo("{\"comm\":{\"cv\":4747474,\"ct\":24,\"format\":\"json\",\"inCharset\":\"utf-8\",\"outCharset\":\"utf-8\",\"notice\":0,\"platform\":\"yqq.json\",\"needNewCode\":1,\"uin\":2488288090,\"g_tk_new_20200303\":31370172,\"g_tk\":31370172},\"req_1\":{\"module\":\"vkey.GetVkeyServer\",\"method\":\"CgiGetVkey\",\"param\":{\"guid\":\"8405163576\",\"songmid\":[\"0045k2B64FHA4Q\",\"000CRoLd052RcF\",\"003PlPU00Kwv7P\",\"000Jd7nI4CBs0a\",\"0009yM7c3uNKho\",\"001C07tn2asDLj\",\"003Ho2cO2RXwUc\",\"002blN5z2c2UAA\",\"001GDkwy4Lrf3V\",\"003FjvYy0rmHJ8\",\"000XVBL23A0Q30\",\"002D9Tle23gYKn\",\"002DBLd50RMYKG\",\"002jysVT2p5bHG\",\"003wRpXN1ez61I\",\"00103KhK09eKtJ\",\"00154Nar3lteVn\",\"002Gb1wH1b2utj\",\"001v3wxx2vDPwQ\",\"003v8eVe2PXgEg\",\"000gnH4O0yyIgM\",\"000EanES3lXnsQ\",\"001Ee9lE0Tux5F\",\"00380zo20BtFHw\",\"004TUQNE2j6Y5n\",\"0000JeVC3rx4LT\",\"002HUu5K06LIDt\",\"001HNFv7140hki\",\"000Wraz33Afn7u\",\"0004aprE286HYQ\",\"0046Tk6T4QMXPu\",\"002orTbT044y9b\",\"000seBcd2Sfmkj\",\"001XoA9n2H1dt7\",\"001S8IK0025RMa\",\"001JNVS01TQ0Kl\",\"000KWkOy0lFa2B\",\"000DaZeG27e1R3\",\"000YXu8N3pYh79\",\"0032itya1gTKfX\",\"002gbOfA1GrDjg\",\"000nqiRU1HB2AE\",\"001cZvJ60IESMT\",\"003FLrMt21DLdA\",\"000AITeM0cPIXa\",\"001KhlRy1S93mI\",\"002tN5Cy26mA0g\",\"000y4pMO2fcK82\",\"000WUoLT0GNCxV\",\"004ee8DS2o3Q4G\",\"001wWjV94gP5rf\",\"004B7Mgi44C2X9\",\"0040KX8M2ZkIw6\",\"001WI6JE2jrMg4\",\"002IHupV32WCDY\",\"000FqSZZ0KBqeW\",\"000kTwMH0sqcP8\",\"0008teHd2YMXJu\",\"0036EhGM4Hrq92\",\"000z6VjY0BpY1p\",\"002NAi7U4XphUV\",\"003hoXJu1whmzB\",\"001vDBuv3U3xiZ\",\"001D9yOR3ON4Q6\",\"004H9C6Z2o0xTr\",\"000kbQpO2TxvJW\",\"004ThoEx1dRUCC\",\"0027dQgi3sHyM8\",\"003rrBvh0jQqN8\",\"004YUy0419YQZy\",\"000Dw49I15VPuq\",\"000fYSIn4Oc9NX\",\"004YjaOB3CT8Ay\",\"004GktvH3aVRMi\",\"000v857K1WPzq9\",\"003Ux1ct14nCJw\",\"000XTEWN47ciTL\",\"002XRWPR06UhZ6\",\"003wTp014S0zQy\",\"002lKFlC3KxIrW\",\"004bBm2g2rQWjd\",\"002eJroI4U2qap\",\"002DVsmJ0aJxjg\",\"002Icx2y0vcm4e\",\"001dbxcj2Ae3uu\",\"003H0DrV24y5GU\",\"000C5ZJZ29nNGL\",\"000oj6bZ2a4q1V\",\"001m4mmf2qAZ3U\"],\"songtype\":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"uin\":\"2488288090\",\"loginflag\":1,\"platform\":\"20\"}},\"req_2\":{\"module\":\"music.musicasset.SongFavRead\",\"method\":\"IsSongFanByMid\",\"param\":{\"v_songMid\":[\"0045k2B64FHA4Q\",\"000CRoLd052RcF\",\"003PlPU00Kwv7P\",\"000Jd7nI4CBs0a\",\"0009yM7c3uNKho\",\"001C07tn2asDLj\",\"003Ho2cO2RXwUc\",\"002blN5z2c2UAA\",\"001GDkwy4Lrf3V\",\"003FjvYy0rmHJ8\",\"000XVBL23A0Q30\",\"002D9Tle23gYKn\",\"002DBLd50RMYKG\",\"002jysVT2p5bHG\",\"003wRpXN1ez61I\",\"00103KhK09eKtJ\",\"00154Nar3lteVn\",\"002Gb1wH1b2utj\",\"001v3wxx2vDPwQ\",\"003v8eVe2PXgEg\",\"000gnH4O0yyIgM\",\"000EanES3lXnsQ\",\"001Ee9lE0Tux5F\",\"00380zo20BtFHw\",\"004TUQNE2j6Y5n\",\"0000JeVC3rx4LT\",\"002HUu5K06LIDt\",\"001HNFv7140hki\",\"000Wraz33Afn7u\",\"0004aprE286HYQ\",\"0046Tk6T4QMXPu\",\"002orTbT044y9b\",\"000seBcd2Sfmkj\",\"001XoA9n2H1dt7\",\"001S8IK0025RMa\",\"001JNVS01TQ0Kl\",\"000KWkOy0lFa2B\",\"000DaZeG27e1R3\",\"000YXu8N3pYh79\",\"0032itya1gTKfX\",\"002gbOfA1GrDjg\",\"000nqiRU1HB2AE\",\"001cZvJ60IESMT\",\"003FLrMt21DLdA\",\"000AITeM0cPIXa\",\"001KhlRy1S93mI\",\"002tN5Cy26mA0g\",\"000y4pMO2fcK82\",\"000WUoLT0GNCxV\",\"004ee8DS2o3Q4G\",\"001wWjV94gP5rf\",\"004B7Mgi44C2X9\",\"0040KX8M2ZkIw6\",\"001WI6JE2jrMg4\",\"002IHupV32WCDY\",\"000FqSZZ0KBqeW\",\"000kTwMH0sqcP8\",\"0008teHd2YMXJu\",\"0036EhGM4Hrq92\",\"000z6VjY0BpY1p\",\"002NAi7U4XphUV\",\"003hoXJu1whmzB\",\"001vDBuv3U3xiZ\",\"001D9yOR3ON4Q6\",\"004H9C6Z2o0xTr\",\"000kbQpO2TxvJW\",\"004ThoEx1dRUCC\",\"0027dQgi3sHyM8\",\"003rrBvh0jQqN8\",\"004YUy0419YQZy\",\"000Dw49I15VPuq\",\"000fYSIn4Oc9NX\",\"004YjaOB3CT8Ay\",\"004GktvH3aVRMi\",\"000v857K1WPzq9\",\"003Ux1ct14nCJw\",\"000XTEWN47ciTL\",\"002XRWPR06UhZ6\",\"003wTp014S0zQy\",\"002lKFlC3KxIrW\",\"004bBm2g2rQWjd\",\"002eJroI4U2qap\",\"002DVsmJ0aJxjg\",\"002Icx2y0vcm4e\",\"001dbxcj2Ae3uu\",\"003H0DrV24y5GU\",\"000C5ZJZ29nNGL\",\"000oj6bZ2a4q1V\",\"001m4mmf2qAZ3U\"]}},\"req_3\":{\"module\":\"music.musichallSong.PlayLyricInfo\",\"method\":\"GetPlayLyricInfo\",\"param\":{\"songMID\":\"0045k2B64FHA4Q\",\"songID\":382816367}},\"req_4\":{\"method\":\"GetCommentCount\",\"module\":\"music.globalComment.GlobalCommentRead\",\"param\":{\"request_list\":[{\"biz_type\":1,\"biz_id\":\"382816367\",\"biz_sub_type\":0}]}},\"req_5\":{\"module\":\"music.musichallAlbum.AlbumInfoServer\",\"method\":\"GetAlbumDetail\",\"param\":{\"albumMid\":\"0013VADl1S6m8A\"}},\"req_6\":{\"module\":\"vkey.GetVkeyServer\",\"method\":\"CgiGetVkey\",\"param\":{\"guid\":\"5109528373\",\"songmid\":[\"0045k2B64FHA4Q\"],\"songtype\":[0],\"uin\":\"2488288090\",\"loginflag\":1,\"platform\":\"20\"}},\"req_7\":{\"module\":\"music.trackInfo.UniformRuleCtrl\",\"method\":\"CgiGetTrackInfo\",\"param\":{\"ids\":[382816367,201260252,203705374,384711659,406034797,201266855,473137,388750337,237266611,283917728,425164909,421073064,424728186,424960612,402189410,424949159,424687429,424651105,424719119,425174584,425007401,425009993,424939988,424209173,425217162,425218397,425195481,421448709,424415867,424300033,424109918,424970358,424663501,424651405,424144714,423631813,424957033,424950910,424324275,420204524,420296449,424313928,421521313,424537147,419052336,424537131,424423378,424417303,424417724,424961896,424455505,424416061,424388331,424415586,424389231,424169524,424459797,424687815,424725172,424717640,424083893,423116267,423942353,424992217,420006062,420387218,425129666,425129563,425192326,425159864,424759908,424653092,424947410,424458785,425205074,423120157,424991715,424959168,424162303,424408874,424682318,425129859,425130500,424930252,424916375,424432365,425130890,424345259,422031797],\"types\":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}}}", "https://u.y.qq.com/cgi-bin/musics.fcg?_=1690709719690&sign=zzbd63bd578r643vgdgqjaiqnggfgnhwf538b015");
        JSONObject musicInfoJson = JSON.parseObject(musicInfoStr, JSONObject.class);
        JSONObject musicFileJson = JSON.parseObject(musicFileStr, JSONObject.class);
        if (ObjUtil.isNotNull(musicInfoJson) && ObjUtil.isNotNull(musicFileJson)) {
            if (musicInfoJson.getIntValue("code") == 0 && musicFileJson.getIntValue("code") == 0) {
                JSONArray musicFileArray = musicFileJson.getJSONObject("req_1").getJSONObject("data").getJSONArray("midurlinfo");
                JSONArray tracks = musicInfoJson.getJSONObject("req_1").getJSONObject("data").getJSONArray("tracks");
                for (int i = 0; i < tracks.size(); i++) {
                    JSONObject fileJson = musicFileArray.getJSONObject(i);
                    MusicVo musicVo = new MusicVo();
                    JSONObject trackJson = tracks.getJSONObject(i);
                    String name = trackJson.getString("name");
                    String artist = getArtist(trackJson);
                    String cover = getCover(trackJson);
                    String url = getFileUrl(fileJson);
                    musicVo.setName(name);
                    musicVo.setCover(cover);
                    musicVo.setArtist(artist);
                    musicVo.setUrl(url);
                    musicVos.add(musicVo);
                }
            }
        }
        return musicVos;
    }

    private String getFileUrl(JSONObject fileJson) {
        String purl = fileJson.getString("purl");
        return "https://dl.stream.qqmusic.qq.com/" + purl;
    }

    private String getQQMusicInfo(String params, String url) {
        HttpHeaders headers = new HttpHeaders();
        String qqCookie = configService.getConfig("MUSIC_QQ_COOKIE", String.class);
        headers.add("Cookie", qqCookie);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>(params, headers);
        return restTemplate.postForObject(url, entity, String.class);
    }

    private String getCover(JSONObject trackJson) {
        JSONObject album = trackJson.getJSONObject("album");
        String pmid = album.getString("pmid");
        return "https://y.qq.com/music/photo_new/T002R300x300M000" + pmid + ".jpg?max_age=2592000";
    }

    private String getArtist(JSONObject trackJson) {
        StringBuilder sb  = new StringBuilder();
        JSONArray singer = trackJson.getJSONArray("singer");
        for (int i = 0; i < singer.size(); i++) {
            JSONObject singerJson = singer.getJSONObject(i);
            sb.append(singerJson.getString("name"));
            if (i != singer.size() - 1) {
                sb.append("/");
            }
        }
        return sb.toString();
    }
}
