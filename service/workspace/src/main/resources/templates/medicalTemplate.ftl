<!DOCTYPE html>
<html lang = 'en'>
    <head>
        <meta charset='utf-8'/>
        <meta name ='viewport' content='width=device-width, inital-scale=1.0'/>
        <style>
            .table{
                width: 80%;
                margin: 0 auto;
            }
            .line1{
                border-bottom: 1px solid black;
                width: 100%;
                margin: 2px auto;
            }
            .line2{
                border-bottom: 2px solid black;
                width: 100%;
                margin: 2px auto;
            }
            .line3{
                border-bottom: 3px solid black;
                width: 100%;
                margin: 2px auto;
            }
            h1{
                text-align: center;
            }
            .left{
                float: left;
                width: 50%;
                height: 30px;
                line-height: 30px;
            }
            .right{
                float: right;
                width: 50%;
                height: 30px;
                line-height: 30px;
            }
            .clear{
                clear: both;
            }
            .txt{
                font-weight: bolder;
            }
        </style>
        <title>病例</title>
    </head>
    <body >
        <div class="table">
            <h1>体检报告单</h1>
            <div style="float: left;">姓名: ${name}</div>
            <div style="float: right;">预约单号: ${reservationCode}</div>
            <div class="clear"></div>
            <div class="line3"></div>
            <div class="line2"></div>
            <div class="left txt">项目</div>
            <div class="left txt">结果</div>
            <div class="clear"></div>
            <div class="line1"></div>
            <#list p?keys as key>
                <div class="left">${key}</div>
                <div class="right">${p[key]}</div>
                <div class="clear"></div>
            </#list>
            <div class="clear"></div>
            <div class="left txt">采样时间: ${medicalDate}</div>
            <div class="left txt">医生: ${doctorName}</div>
            <div class="line1"></div>
            <div class="clear"></div>
            <div class="line2"></div>
            <div class="line3"></div>
        </div>
        <script>
        </script>
    </body>
</html>