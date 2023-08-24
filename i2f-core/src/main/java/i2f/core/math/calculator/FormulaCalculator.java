package i2f.core.math.calculator;
/*
--------------------------
name:公式计算器
author:Ugex.Savelar
date:2020-7-25
--------------------------
dev-src:cpp
re-impl:java
--------------------------
基于双栈（数字栈，符号栈）实现的字符串公式计算器
原理：
扫描整个输入公式，从左向右扫描
如果是数字，直接入栈到数字栈
如果是符号
	如果符号栈为空，或者是左括号，无条件入栈到符号栈
	如果符号栈不为空
		和符号栈栈顶符号，比较优先级
			如果高于栈顶优先级，直接入栈到符号栈
			如果低于栈顶优先级，从数字栈取出两个数字，从符号栈取出一个符号，进行运算，
				将运算结果放入数字栈，将当前符号入栈到符号栈
			如果当前扫描到的符号是右括号）
				不断取出两数字和一符号运算，知道符号栈栈顶符号为左括号（
				弹出栈顶的左括号（
也就是说，数字和左括号（无条件入栈，右括号）始终不入栈，
遇到右括号），就不断弹栈计算，知道消去栈中弹出的第一个左括号（，
其他时候，当前的符号优先级高，直接入栈，优先级低则弹栈计算，再入栈

伪代码流程：
	直到 输入公式扫描结束
		如果 扫描到字符
			获取数字
			入栈-数字栈
			移动下标
		否则
			获取当前符号
			如果 不支持当前符号
				退出循环、直接返回
			如果 符号栈空
				入栈当前符号
			否则
				获取符号栈顶符号
				获得栈顶符号和当前符号的优先级
				如果 优先级获取出错的标志
					退出循环、直接返回
				否则如果 消除括号的优先级标志
					出栈符号栈
				否则如果 当前符号优先级 低于 栈顶符号
					出栈两个数字和一个符号进行计算，并将计算结果重新入栈数字栈--查看注解1
					如果 当前符号 是 右括号
						继续下一轮循环，跳过本轮循环
					入栈 当前符号 到 符号栈
				否则如果 当前符号优先级 高于 栈顶符号
					入栈 当前符号 到 符号栈
			移动下标
	直到 符号栈 为空
		出栈两个数字和一个符号进行计算，并将计算结果重新入栈数字栈--查看注解1

	返回 数字栈栈顶，这就是最终的结果

-----注解1：
	出栈数字2 从 数字栈
	出栈数字1 从 数字栈
	出栈运算符号 从 符号栈
	计算 数字1 运算符号 数字2 保存到 结果
	入栈 结果 到 数字栈
----注意，这里是区分了数字1和数字2的，因为除法减法是需要区分的
*/

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FormulaCalculator {

    public static final MathContext DEFAULT_CONTEXT = new MathContext(16, RoundingMode.HALF_UP);
    public static final SecureRandom RANDOM = new SecureRandom();
    public static final BigDecimal NUM_PI = new BigDecimal("3.141592653549626");
    /////////////////////////////////////////////
    public static final BigDecimal NUM_E = new BigDecimal("2.718281828459045");
    public static final BigDecimal NUM_GSEC = new BigDecimal("0.618033988749894");
    public static final BigDecimal NUM_180 = new BigDecimal("180.0");
    public static final BigDecimal NUM_100 = new BigDecimal("100.0");
    public static final BigDecimal NUM_2 = new BigDecimal("2.0");
    private int p_addCount;
    private int m_indexOfSingleNumberOperatorBegin;
    private char[] m_ruleTable;
    private List<String> m_flags;
    private Stack<BigDecimal> m_numberStack;
    private Stack<String> m_flagStack;

    public FormulaCalculator() {
        m_flags = new ArrayList<>();
        m_ruleTable = null;
        m_flagStack = new Stack<>();
        m_numberStack = new Stack<>();
        m_indexOfSingleNumberOperatorBegin = 0;

        // 加、减、乘、除、取模、求幂
        m_flags.add("+");    //写法参考：3+2-5*4/3
        m_flags.add("-");
        m_flags.add("*");
        m_flags.add("/");
        m_flags.add("%");
        m_flags.add("^");
        m_flags.add("(");
        m_flags.add(")");

        // N次根、对数、累加、累乘
        m_flags.add("sqrt");    // 对m开n次根: n sqrt m //写法参考：2 sqrt 4 + 2 log 8 adds 3 ，其实和传统运算符一样，操作数在两边
        m_flags.add("log");    // 计算log以a为底b的对数： a log b //log(num2)/log(num1)//换底公式
        m_flags.add("adds");    //计算a累加到b: a adds b
        m_flags.add("muls");    //计算a累乘到b: a muls b

        // 位与、位或、位异或
        m_flags.add("and");    //二进制运算
        m_flags.add("or");
        m_flags.add("xor");

        // 左移、右移、最大值、最小值、平均值、1/n累加到1/m、1/n累乘到1/m、最小公倍数、最大公约数、增长百分比、降低百分比、区间随机值、指定年的指定月的总天数
        m_flags.add("lmov");
        m_flags.add("rmov");
        m_flags.add("max");
        m_flags.add("min");
        m_flags.add("avg");
        m_flags.add("recipadds");
        m_flags.add("recipmuls");
        m_flags.add("minsum");
        m_flags.add("maxfac");
        m_flags.add("peradd");
        m_flags.add("persub");
        m_flags.add("rand");
        m_flags.add("dayofmonth");

        m_indexOfSingleNumberOperatorBegin = m_flags.size(); //单目双目运算符分割记录

        // 按位取反 阶乘 负号 百分号 绝对值 转弧度 转角度 三角函数族
        m_flags.add("not");
        m_flags.add("!");        //阶乘		//写法参考：2! + (6 neg) abs ，其实和阶乘写法一样，操作数在前
        m_flags.add("neg");    //取负数	//负号
        m_flags.add("per");    //取百分值	//百分号
        m_flags.add("abs");    //取绝对值
        m_flags.add("radian");    //取弧度制
        m_flags.add("angle");        //取角度制
        m_flags.add("sin");        //三角函数族，默认使用弧度进行计算，特别说明
        m_flags.add("cos");
        m_flags.add("tan");
        m_flags.add("arcsin");
        m_flags.add("arccos");
        m_flags.add("arctan");

        // 求倒数 e的x次幂 自然对数 n倍圆周率 n倍自然常数 n倍黄金分割率
        m_flags.add("recip");
        m_flags.add("epow");
        m_flags.add("ln");
        m_flags.add("numpi");
        m_flags.add("nume");
        m_flags.add("numgsec");

        // x的x次方 从0-m区间随机整数 n倍随机0-1之间小数 上取整 下取整 四舍五入 取整数部分 取小数部分 某年一共多少天 km/h转m/s m/s转km/h 华氏转摄氏 摄氏转华氏 斐波那契第N项
        m_flags.add("xpowx");
        m_flags.add("randz");
        m_flags.add("randf");
        m_flags.add("ceil");
        m_flags.add("floor");
        m_flags.add("round");
        m_flags.add("dhead");
        m_flags.add("ftail");
        m_flags.add("dayofyear");
        m_flags.add("kmhtoms");
        m_flags.add("mstokmh");
        m_flags.add("fttoct");
        m_flags.add("cttoft");
        m_flags.add("feibo");

        int len = m_flags.size();
        m_ruleTable = new char[len * len];

        char[][] tpTable =
                {
                        //	+		-		*		/		%		^		(		)			sqrt		log		adds		muls			and		or		xor			lmov		rmov		max		min		avg		recipadds		recipmuls		minsum		maxfac		peradd		persub		rand		dayofmonth			not		!		neg		per		abs		radian		angle		sin		cos		tan		arcsin		arccos		arctan			recip		epow		ln		numpi		nume		numgsec			xpowx		randz		randf		ceil		floor		round		dhead		ftail		dayofyear		kmhtoms		mstokmh		fttoct		cttoft		feibo	},	//	外部
                        {0, 0, 0, 0, 0, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	+	*/
                        {0, 0, 0, 0, 0, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	-	*/
                        {1, 1, 0, 0, 0, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	*	*/
                        {1, 1, 0, 0, 0, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	/	*/
                        {1, 1, 1, 1, 1, 1, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	%	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	^	*/
                        {1, 1, 1, 1, 1, 1, 1, 99, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},    /*	(	*/
                        {0, 0, 0, 0, 0, 0, 9, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	)	*/
                        //	拓展双目																																																																																																																																//
                        {1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	sqrt	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	log	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	adds	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	muls	*/

                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	and	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	or	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	xor	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	lmov	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	rmov	*/
                        {0, 0, 0, 0, 0, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	max	*/
                        {0, 0, 0, 0, 0, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	min	*/
                        {0, 0, 0, 0, 0, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	avg	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	recipadds	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	recipmuls	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	minsum	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	maxfac	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	peradd	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	persub	*/
                        {1, 1, 0, 0, 0, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	rand	*/
                        {1, 1, 0, 0, 0, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	dayofmonth	*/
                        //	单目运算																																																																																																																																//
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	not	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	!	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	neg	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	per	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	abs	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	radian	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	angle	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	sin	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	cos	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	tan	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	arcsin	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	arccos	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	arctan	*/
                        //	拓展单目																																																																																																																																//
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	recip	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	epow	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	ln	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	numpi	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	nume	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	numgsec	*/

                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	xpowx	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	randz	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	randf	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	ceil	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	floor	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	round	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	dhead	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	ftail	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	dayofyear	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	kmhtoms	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	mstokmh	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	fttoct	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	cttoft	*/
                        {1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    /*	feibo	*/

                };

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                ruleAt2(i, j, tpTable[i][j]);
            }
        }

    }

    public static String buildPrepareFormula(String preFormula, List<String> flags, List<String> values) {
        String ret = preFormula;
        for (int i = 0; i < flags.size(); i++) {
            ret = ret.replaceAll(flags.get(i), values.get(i));
        }
        return ret;
    }

    public static double hex2Number(String str, int base) {
        return new FormulaCalculator().getNumber(str, base).doubleValue();
    }

    public static String number2Hex(double num, int base, int decimal) {
        StringBuilder hex = new StringBuilder();
        if (base < 2 || base > 16 || decimal < 0) {
            return hex.toString();
        }
        String map = "0123456789ABCDEF";

        char[] temp = new char[128];
        int otc = (int) num;
        double flo = num - otc;
        int len = 0;
        while (otc != 0) {
            int pnum = otc % base;
            temp[len++] = map.charAt(pnum);
            otc = (int) otc / base;
        }
        int tlen = len - 1;
        if (tlen <= 0) {
            hex.append('0');
        }
        while (tlen >= 0) {
            hex.append(temp[tlen]);
            tlen--;
        }
        hex.append('.');
        int dotIndex = hex.length();
        int i = 0;
        while (flo != 0.0 && i < decimal) {
            int pnum = (int) (flo * base);
            hex.append(map.charAt(pnum));
            flo = flo * base - pnum;
            i++;
        }
        if (dotIndex == hex.length()) {
            hex.append('0');
        }

        return hex.toString();
    }

    public static String getUseHelpStr() {
        String help = "" +
                "公式计算器使用简介：\tUgex.Savelar\n" +
                "\t常规双目运算符号：+ - * / % ^ ( )\n" +
                "\t\t加、减、乘、除、取模、求幂\n" +
                "\t\t用法：3+2*5-(2^3)%5\n" +
                "\t拓展双目运算符号：sqrt log adds muls and or xor lmov	rmov	max	min avg	recipadds	recipmuls	minsum	maxfac	peradd	persub	rand	dayofmonth\n" +
                "\t\tN次根、对数、累加、累乘、位与、位或、位异或、左移、右移、最大值、最小值、平均值、1/n累加到1/m、1/n累乘到1/m、最小公倍数、最大公约数、增长百分比、降低百分比、区间随机值、指定年的指定月的总天数\n" +
                "\t\t用法：2 sqrt 4 + 1adds5+ 7 and 3 + 2 lmov 2 + 4 rmov 1 + 5 max 3 + 3 min 4 + 5 avg 3 + 2 recipadds 5 + 2 recipmuls 5 + 2 minsum 3 + 12 maxfac 6 + 100 peradd 2.3 + 100 persub 1.98 + 0 rand 10 + 2021 dayofmonth 2\n" +
                "\t\t说明：2sqrt4表示4开2次根，1adds5表示1+...+5\n" +
                "\t\t\t2log8表示求以2为底8的对数,7and3表示7位与3\n" +
                "\t拓展单目运算符：\n" +
                "\t\tnot ! neg per abs radian angle sin cos tan arcsin arccos arctan recip epow ln numpi nume numgsec xpowx	randz	randf	ceil	floor	round	dhead	ftail	dayofyear	kmhtoms	mstokmh	fttoct	cttoft	feibo\n" +
                "\t\t按位取反 阶乘 负号 百分号 绝对值 转弧度 转角度 三角函数族 求倒数 e的x次幂 自然对数 n倍圆周率 n倍自然常数 n倍黄金分割率 x的x次方 从0-m区间随机整数 n倍随机0-1之间小数 上取整 下取整 四舍五入 取整数部分 取小数部分 某年一共多少天 km/h转m/s m/s转km/h 华氏转摄氏 摄氏转华氏 斐波那契第N项\n" +
                "\t\t用法：3!+5neg+50per+(60radian)sin\n" +
                "\t\t说明：3!表示求3的阶乘，5neg表示负5,50per表示百分之50\n" +
                "\t\t\t60radian表示将60转换为弧度制，(60radian)sin表示求sin60角度\n" +
                "\t\t[注意]:三角函数族计算需要使用弧度制，拥有运算符转换\n" +
                "\t内建符号：\n" +
                "\t\tdehex\n" +
                "\t\t解析2-16进制数，用法：16 dehex0c\n" +
                "\t\t【注意】：dehex【之后】紧跟解析数，不能有空格等符号，否则出错\n" +
                "\t\t【注意】：dehex【之前】可以有空格等符号，和其他运算符一致\n" +
                "\t\t出错写法：16 dehexc ,16 dehex 0c\n" +
                "\t\t正确写法：16dehex0c ,8 dehex14 ,2dehex1100\n" +
                "\t\t如果该符号之后第一个有效字符不是0-9，请在前面加上0，如上的c就是这个情况\n" +
                "\t\t否则将会按照运算符去处理识别，而出错\n" +
                "\t注意事项：\n" +
                "\t\t运算符不区分大小写\n" +
                "\t\t空格空白符号可以任意添加，只要不中断运算符\n" +
                "\t\t空白符号指：ASCII字符：空格 回车 换行 制表符\n" +
                "\t\t计算结果请检查isSuccess标志位，如果为false，计算失败，错误发生\n" +
                "\t\t你可以使用getLastErrStr()获取错误描述\n" +
                "\t\t使用getLastResult()获取计算结果，如果发生错误就是中间计算结果\n" +
                "\t\t\t如果未发生错误，就是正确结果\n" +
                "\t综合使用案例：\n" +
                "\t\t7 and 3 + 1 or 2 + 3 xor 0 + (0 not) and 3 + " +
                "(60radian)sin + (30radian)cos + (30radian)tan + 3!+ 25per + 10neg + 180radian + " +
                "2 sqrt 4 + 2 log 8 + 1 adds 5 + 1 muls 3 + " +
                "((3.14 * 2 - 6 / (2 % 10)) + (3 ^ 1)) / (4 / 2) + " +
                "((3 + 5 * (4 % (3 + 7)) / (2 ^ 2)) / 2 / 2) ^ 3 + " +
                "3 * 2 * 2 / 4 + 5 * (2 - 4) + 6 / 3 + 12 % 10 + 2 ^ 3+" +
                "1 recip + 1 epow + 1 nume ln + 2 numpi + 1 numgsec+" +
                "16 dehex0c + 8 dehex14 - 2 dehex1100\n" +
                "\t\t计算结果为：79.460495\n" +
                "";
        return help;
    }

    private static int getNumCharValue(char ch, int base) {
        String map = "0123456789ABCDEF";
        if (ch >= 'a' && ch <= 'z') {
            ch &= ~32;
        }
        int i = 0;
        while (i < map.length() && i < base) {
            if (ch == map.charAt(i)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private static boolean isNumberChar(char ch, int base) {
        String map = "0123456789ABCDEF";
        if (ch >= 'a' && ch <= 'z') {
            ch &= ~32;
        }
        int i = 0;
        while (i < map.length() && i < base) {
            if (ch == map.charAt(i)) {
                return true;
            }
            i++;
        }
        return false;
    }

    public static long feiboSeqIdx(long idx) {
        if (idx <= 2) {
            return 1;
        }
        return feiboSeqIdx(idx - 1) + feiboSeqIdx(idx - 2);
    }

    public static boolean isLeapYear(long year) {
        if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
            return true;
        }
        return false;
    }

    public BigDecimal calculate(String formula) {
        return calculate(formula, 10);
    }

    /*
    给定算术表达式，即可进行计算，返回计算结果，
    并且你需要接受计算是否成功的标志，
    一旦发生错误，返回值为0，成功标志为false
    但是你可以通过getLastErrStr()获取错误描述
    可能过程中，是存在一定步骤之前的正确结果的，
    你可以通过getLastResult()获取计算结果，此方法如果之前计算标志成功，那么就是最终的计算结果
    base参数用于指定运算使用的进制，默认十进制，这将会决定整个计算的数字部分的解析
    */
    public BigDecimal calculate(String formula, int base) {
        m_flagStack.clear();
        m_numberStack.clear();

        if (formula.trim().length() == 0) {
            throw new RuntimeException("输入串长度被截断或是无意义空串，请重试");
        }

        int i = 0;
        int formulaLen = formula.length();
        while (i < formulaLen) {
            p_addCount = 0;
            if (isNumberChar(formula.charAt(i), base)) {
                BigDecimal num = getNumber(formula.substring(i), base);
                m_numberStack.push(num);
                i += p_addCount;
            } else {
                String flg = getFlag(formula.substring(i), base);
                flg = flg.trim();
                if (flg.length() > 0) {
                    if ("dehex".equalsIgnoreCase(flg)) {
                        int baseCur = Integer.parseInt(String.valueOf(m_numberStack.pop().intValue()));
                        int bakAddCount = p_addCount;
                        p_addCount = 0;
                        BigDecimal num = getNumber(formula.substring(i + bakAddCount), baseCur);
                        m_numberStack.push(num);
                        i += bakAddCount + p_addCount;
                        p_addCount = bakAddCount;
                        continue;
                    }
                    if (!isLegalOperator(flg)) {
                        throw new RuntimeException(String.format("未识别的运算符：%s", flg));
                    }

                    if (m_flagStack.isEmpty()) {
                        m_flagStack.push(flg);
                    } else {
                        String top = m_flagStack.peek();
                        int priority = getPriortyTag(flg, top);
                        if (priority == 99) {
                            throw new RuntimeException(String.format("表达式错误：index:%d,value:%s", i, flg));
                        } else if (priority == 9) {
                            m_flagStack.pop();
                        } else if (priority == 0) {
                            if (!calculateOnceStack()) {
                                throw new RuntimeException(String.format("公式错误，不匹配的操作数,操作数个数异常：index:%d,value:%s", i, flg));
                            }
                            //注意，这个continue至关重要，主要作用是处理最里层的括号内的运算
                            //由于后面的下标移动是统一的，如果这里不判断进行continue
                            //将会导致下标持续向前走，导致计算括号出现错误
                            if (")".equalsIgnoreCase(flg)) {
                                continue;
                            }
                            m_flagStack.push(flg);
                        } else if (priority == 1) {
                            m_flagStack.push(flg);
                        }
                    }

                }
                i += p_addCount;
            }
        }
        //处理最后栈中剩余的数据
        //特别注意：此时栈中的符号依然存在优先级关系，如果直接不断取出栈顶计算，那么就算没有优先级，也会应为不遵循从左到右的计算规则而导致计算出错
        //常见的：2*2-4*1+2 sqrt 4，将会导致计算流程变为：((2*2)-((4*1)+(2 sqrt 4)))=-2,而不是正确的结果2=(((2*2)-(4*1))+(2 sqrt 4))
        //这种错误将是致命的，因此，正确的善后计算过程是：
		/*
		如果 符号栈内数量大于等于2， 那就需要比较优先级，下面的描述中：真栈顶==真实的栈顶部元素，次栈顶==除了栈顶的栈顶元素
			如果 真栈顶的优先级 大于 次栈顶的优先级
				那么 直接弹出数字栈两个数字和符号栈一个符号进行计算，将计算结果入栈【符号不在入栈了，需要注意】
			否则
				那么 先保存符号栈顶和数字栈的栈顶，在去正常的弹栈计算，将计算结果入栈【再把先前保存的符号和数字入栈】
		否则
			直接弹栈计算，得出最终结果

		*/
        while (!m_flagStack.isEmpty()) {
            boolean isCalSec = false;
            if (m_flagStack.size() >= 2)//如果符号栈还有两个以上的运算符，那就需要优先级比较
            {
                //先弹栈比较优先级
                String endTop = m_flagStack.pop();
                String preTop = m_flagStack.pop();
                int priority = getPriortyTag(endTop, preTop);
                if (priority == 1) {
                    //真栈顶的优先级高，则恢复栈
                    m_flagStack.push(preTop);
                    m_flagStack.push(endTop);

                    //弹栈计算,入栈结果
                    isCalSec = calculateOnceStack();
                } else if (priority == 0) {
                    //真栈顶的优先级低，则保存两栈的栈顶，因为我这里实现的时候，是弹栈比较的优先级，因此需要回复次栈顶成为临时栈顶
                    BigDecimal topNum = m_numberStack.pop();
                    m_flagStack.push(preTop);

                    //弹栈计算，入栈结果
                    isCalSec = calculateOnceStack();

                    //恢复保存的两真栈顶
                    m_numberStack.push(topNum);
                    m_flagStack.push(endTop);
                } else {
                    //由于最后收尾的时候，符号栈不存在左括号了，但是如果还是存在，那就是出错了
                    throw new RuntimeException("堆栈异常或者符号栈异常");
                }
            } else {
                //如果符号栈只有一个符号了，没得商量，直接计算，得到最终结果
                isCalSec = calculateOnceStack();
            }

            if (!isCalSec) {
                throw new RuntimeException("公式错误，不匹配的操作数,操作数个数异常：index:end,value:end");
            }
        }

        if (m_numberStack.isEmpty()) {
            throw new RuntimeException("不能识别的公式，无运算结果");
        }
        if (m_numberStack.size() != 1) {
            throw new RuntimeException("公式存在错误或不完整，但是依然满足计算需求规则");
        }
        return m_numberStack.peek();
    }

    public BigDecimal getLastResult() {
        if (m_numberStack.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return m_numberStack.peek();
    }

    private String getFlag(String str, int base) {
        StringBuilder ret = new StringBuilder();
        int strLen = str.length();
        if (isNumberChar(str.charAt(0), base)) {
            p_addCount = 0;
            return ret.toString();
        }
        int i = 0;
        if (!(str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')
                && !(str.charAt(i) >= 'a' && str.charAt(i) <= 'z')) {
            ret.append(str.charAt(i));
            p_addCount = 1;
            return ret.toString();
        }
        while (i < strLen && !isNumberChar(str.charAt(i), base)) {
            if (!(str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')
                    && !(str.charAt(i) >= 'a' && str.charAt(i) <= 'z')) {
                break;
            }
            ret.append(str.charAt(i));
            i++;
        }
        p_addCount = i;
        return ret.toString();
    }

    private BigDecimal getNumber(String str, int base) {
        BigDecimal ret = BigDecimal.ZERO;
        BigDecimal baseNum = BigDecimal.valueOf(base);
        if (!isNumberChar(str.charAt(0), base)) {
            p_addCount = 0;
            return BigDecimal.ZERO;
        }
        int i = 0;
        while (i < str.length() && isNumberChar(str.charAt(i), base)) {
            ret = ret.multiply(baseNum, DEFAULT_CONTEXT);
            int cnum = getNumCharValue(str.charAt(i), base);
            ret = ret.add(BigDecimal.valueOf(cnum), DEFAULT_CONTEXT);
            i++;
        }
        if (i < str.length() && str.charAt(i) == '.') {
            i++;
            BigDecimal lbit = BigDecimal.ONE;
            while (i < str.length() && isNumberChar(str.charAt(i), base)) {
                lbit = lbit.multiply(baseNum, DEFAULT_CONTEXT);
                int cnum = getNumCharValue(str.charAt(i), base);
                ret = ret.add(BigDecimal.valueOf(cnum).divide(lbit, DEFAULT_CONTEXT), DEFAULT_CONTEXT);
                i++;
            }
        }
        p_addCount = i;
        return ret;
    }

    private boolean isLegalOperator(String ope) {
        for (int i = 0; i < m_flags.size(); i++) {
            if (ope.equalsIgnoreCase(m_flags.get(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean calculateOnceStack() {
        String top = m_flagStack.peek();
        if (isSingleNumberOperator(top))//单目运算符号，只取出一个操作数计算即可
        {
            if (m_numberStack.size() < 1) {
                return false;
            }
            BigDecimal num2 = BigDecimal.ZERO;
            BigDecimal num1 = m_numberStack.pop();
            String ope = m_flagStack.pop();

            BigDecimal result = calculateOperator(num1, num2, ope);
            m_numberStack.push(result);

        } else {
            if (m_numberStack.size() < 2) {
                return false;
            }
            BigDecimal num2 = m_numberStack.pop();
            BigDecimal num1 = m_numberStack.pop();
            String ope = m_flagStack.pop();

            BigDecimal result = calculateOperator(num1, num2, ope);
            m_numberStack.push(result);
        }

        return true;
    }

    private boolean isSingleNumberOperator(String ope) {
        for (int i = m_indexOfSingleNumberOperatorBegin; i < m_flags.size(); i++) {
            if (ope.equalsIgnoreCase(m_flags.get(i))) {
                return true;
            }
        }
        return false;
    }

    private BigDecimal calculateOperator(BigDecimal num1, BigDecimal num2, String ope) {

        //+	-	*	/	%	^	(	)		sqrt	log	adds	muls		and	or	not	xor		!	neg	per	abs	radian	angle	sin	cos	tan	arcsin	arccos	arctan recip	epow	ln	numpi	nume	numgsec
        if ("+".equalsIgnoreCase(ope)) {
            return num1.add(num2, DEFAULT_CONTEXT);
        } else if ("-".equalsIgnoreCase(ope)) {
            return num1.subtract(num2, DEFAULT_CONTEXT);
        } else if ("*".equalsIgnoreCase(ope)) {
            return num1.multiply(num2, DEFAULT_CONTEXT);
        } else if ("/".equalsIgnoreCase(ope)) {
            return num1.divide(num2, DEFAULT_CONTEXT);
        } else if ("%".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf((num1.longValue()) % (num2.longValue()));
        } else if ("^".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(Math.pow(num1.doubleValue(), num2.doubleValue()));
        } else if ("sqrt".equalsIgnoreCase(ope))//n次根号m==m的1/n次幂
        {
            return BigDecimal.valueOf(Math.pow(num2.doubleValue(), 1.0 / num1.doubleValue()));
        } else if ("log".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(Math.log(num2.doubleValue()) / Math.log(num1.doubleValue()));
        } else if ("adds".equalsIgnoreCase(ope)) {
            BigDecimal ret = BigDecimal.ZERO;
            int step = num1.compareTo(num2) < 0 ? 1 : -1;
            long beg = num1.longValue();
            long end = num2.longValue();
            for (long i = beg; i <= end; i += step) {
                ret = ret.add(BigDecimal.valueOf(i), DEFAULT_CONTEXT);
            }
            return ret;
        } else if ("muls".equalsIgnoreCase(ope)) {
            BigDecimal ret = BigDecimal.ONE;
            int step = num1.compareTo(num2) < 0 ? 1 : -1;
            long beg = num1.longValue();
            long end = num2.longValue();
            for (long i = beg; i <= end; i += step) {
                ret = ret.multiply(BigDecimal.valueOf(i), DEFAULT_CONTEXT);
            }
            return ret;
        } else if ("and".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf((num1.longValue()) & (num2.longValue()));
        } else if ("or".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf((num1.longValue()) | (num2.longValue()));
        } else if ("xor".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf((num1.longValue()) ^ (num2.longValue()));
        } else if ("lmov".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf((num1.longValue()) << (num2.longValue()));
        } else if ("rmov".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf((num1.longValue()) >> (num2.longValue()));
        } else if ("max".equalsIgnoreCase(ope)) {
            return num1.compareTo(num2) > 0 ? num1 : num2;
        } else if ("min".equalsIgnoreCase(ope)) {
            return num1.compareTo(num2) < 0 ? num1 : num2;
        } else if ("avg".equalsIgnoreCase(ope)) {
            return (num1.add(num2, DEFAULT_CONTEXT)).divide(NUM_2, DEFAULT_CONTEXT);
        } else if ("recipadds".equalsIgnoreCase(ope)) {
            BigDecimal ret = BigDecimal.ZERO;
            int step = num1.compareTo(num2) <= 0 ? 1 : -1;
            long beg = num1.longValue();
            long end = num2.longValue();
            for (long i = beg; i <= end; i += step) {
                ret = ret.add(BigDecimal.ONE.divide(BigDecimal.valueOf(i), DEFAULT_CONTEXT));
            }
            return ret;
        } else if ("recipmuls".equalsIgnoreCase(ope)) {
            BigDecimal ret = BigDecimal.ONE;
            int step = num1.compareTo(num2) <= 0 ? 1 : -1;
            long beg = num1.longValue();
            long end = num2.longValue();
            for (long i = beg; i <= end; i += step) {
                ret = ret.multiply(BigDecimal.ONE.divide(BigDecimal.valueOf(i), DEFAULT_CONTEXT));
            }
            return ret;
        } else if ("minsum".equalsIgnoreCase(ope)) {
            BigDecimal ret = new BigDecimal("-1");
            long n1 = Math.abs(num1.longValue());
            long n2 = Math.abs(num2.longValue());
            long min = n1 < n2 ? n1 : n2;
            for (long i = min; i > 0; i++) {
                if (i % n1 == 0 && i % n2 == 0) {
                    ret = BigDecimal.valueOf(i);
                    break;
                }
            }
            return ret;
        } else if ("maxfac".equalsIgnoreCase(ope)) {
            BigDecimal ret = new BigDecimal("-1");
            long n1 = Math.abs(num1.longValue());
            long n2 = Math.abs(num2.longValue());
            long max = n1 > n2 ? n1 : n2;
            for (long i = max; i > 0; i--) {
                if (n1 % i == 0 && n2 % i == 0) {
                    ret = BigDecimal.valueOf(i);
                    break;
                }
            }
            return ret;
        } else if ("peradd".equalsIgnoreCase(ope)) {
            return num1.multiply(BigDecimal.ONE.add(num2.divide(NUM_100, DEFAULT_CONTEXT), DEFAULT_CONTEXT), DEFAULT_CONTEXT);
        } else if ("persub".equalsIgnoreCase(ope)) {
            return num1.multiply(BigDecimal.ONE.subtract(num2.divide(NUM_100, DEFAULT_CONTEXT), DEFAULT_CONTEXT), DEFAULT_CONTEXT);
        } else if ("rand".equalsIgnoreCase(ope)) {
            long min = num1.compareTo(num2) < 0 ? num1.longValue() : num2.longValue();
            return BigDecimal.valueOf(RANDOM.nextLong() % (Math.abs(num1.longValue() - num2.longValue())) + min);
        } else if ("dayofmonth".equalsIgnoreCase(ope)) {
            long ret = -1;
            int year = num1.intValue();
            int month = num2.intValue();
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    ret = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    ret = 30;
                    break;
                case 2:
                    if (isLeapYear(year)) {
                        ret = 29;
                    } else {
                        ret = 28;
                    }
                    break;
                default:
                    ret = -1;
                    break;
            }
            return BigDecimal.valueOf(ret);
        } else if ("not".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(~(num1.longValue()));
        } else if ("!".equalsIgnoreCase(ope)) //!	neg	per	abs	radian	angle	sin	cos	tan	arcsin	arccos	arctan
        {
            BigDecimal ret = BigDecimal.ONE;
            long beg = 1;
            long end = num1.longValue();
            for (long i = beg; i <= end; i++) {
                ret = ret.multiply(BigDecimal.valueOf(i), DEFAULT_CONTEXT);
            }
            return ret;
        } else if ("neg".equalsIgnoreCase(ope)) {
            return BigDecimal.ZERO.subtract(num1, DEFAULT_CONTEXT);
        } else if ("per".equalsIgnoreCase(ope)) {
            return num1.divide(NUM_100, DEFAULT_CONTEXT);
        } else if ("abs".equalsIgnoreCase(ope)) {
            return num1.abs(DEFAULT_CONTEXT);
        } else if ("radian".equalsIgnoreCase(ope)) {
            return num1.divide(NUM_180, DEFAULT_CONTEXT).multiply(NUM_PI, DEFAULT_CONTEXT);
        } else if ("angle".equalsIgnoreCase(ope)) {
            return num1.divide(NUM_PI, DEFAULT_CONTEXT).multiply(NUM_180, DEFAULT_CONTEXT);
        } else if ("sin".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(Math.sin(num1.doubleValue()));
        } else if ("cos".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(Math.cos(num1.doubleValue()));
        } else if ("tan".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(Math.tan(num1.doubleValue()));
        } else if ("arcsin".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(Math.asin(num1.doubleValue()));
        } else if ("arccos".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(Math.acos(num1.doubleValue()));
        } else if ("arctan".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(Math.atan(num1.doubleValue()));
        } else if ("recip".equalsIgnoreCase(ope))//recip	epow	ln	numpi	nume	numgsec
        {
            return BigDecimal.ONE.divide(num1, DEFAULT_CONTEXT);
        } else if ("epow".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(Math.pow(NUM_E.doubleValue(), num1.doubleValue()));
        } else if ("ln".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(Math.log(num1.doubleValue()) / Math.log(NUM_E.doubleValue()));
        } else if ("numpi".equalsIgnoreCase(ope)) {
            return num1.multiply(NUM_PI, DEFAULT_CONTEXT);
        } else if ("nume".equalsIgnoreCase(ope)) {
            return num1.multiply(NUM_E, DEFAULT_CONTEXT);
        } else if ("numgsec".equalsIgnoreCase(ope)) {
            return num1.multiply(NUM_GSEC, DEFAULT_CONTEXT);
        } else if ("xpowx".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(Math.pow(num1.doubleValue(), num1.doubleValue()));
        } else if ("randz".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(RANDOM.nextLong() % (num1.longValue()));
        } else if ("randf".equalsIgnoreCase(ope)) {
            return num1.multiply(BigDecimal.valueOf(RANDOM.nextDouble()), DEFAULT_CONTEXT);
        } else if ("ceil".equalsIgnoreCase(ope)) {
            double fnum = Math.abs(num1.doubleValue() - num1.longValue());
            return BigDecimal.valueOf((num1.longValue()) + (fnum < 10e-8 ? 0 : 1));
        } else if ("floor".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(num1.longValue());
        } else if ("round".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(num1.add(new BigDecimal("0.5"), DEFAULT_CONTEXT).longValue());
        } else if ("dhead".equalsIgnoreCase(ope)) {
            return BigDecimal.valueOf(num1.longValue());
        } else if ("ftail".equalsIgnoreCase(ope)) {
            return num1.subtract(BigDecimal.valueOf(num1.longValue()), DEFAULT_CONTEXT);
        } else if ("dayofyear".equalsIgnoreCase(ope)) {
            long year = num1.longValue();
            return BigDecimal.valueOf(isLeapYear(year) ? 366 : 365);
        } else if ("kmhtoms".equalsIgnoreCase(ope)) {
            return num1.divide(new BigDecimal("3.6"), DEFAULT_CONTEXT);
        } else if ("mstokmh".equalsIgnoreCase(ope)) {
            return num1.multiply(new BigDecimal("3.6"), DEFAULT_CONTEXT);
        } else if ("fttoct".equalsIgnoreCase(ope)) {
            return (num1.subtract(new BigDecimal("32"), DEFAULT_CONTEXT)).divide(new BigDecimal("1.8"), DEFAULT_CONTEXT);
        } else if ("cttoft".equalsIgnoreCase(ope)) {
            return num1.multiply(new BigDecimal("1.8")).add(new BigDecimal("32"));
        } else if ("feibo".equalsIgnoreCase(ope)) {
            long idx = Math.abs(num1.longValue());
            return BigDecimal.valueOf(feiboSeqIdx(idx));
        }
        return BigDecimal.ZERO;
    }

    private int getPriortyTag(String cur, String top) {
        int curI = getFlagIndex(cur);
        int topI = getFlagIndex(top);
        if (curI == -1 || topI == -1) {
            return 99;
        }
        return ruleAt(curI, topI);
    }

    private int getFlagIndex(String flg) {
        int ret = -1;
        for (int i = 0; i < m_flags.size(); i++) {
            if (flg.equalsIgnoreCase(m_flags.get(i))) {
                ret = i;
                break;
            }
        }
        return ret;
    }

    //cur line ,top row
    private char ruleAt(int cur, int top) {
        return m_ruleTable[cur * m_flags.size() + top];
    }

    private void ruleAt2(int cur, int top, char val) {
        m_ruleTable[cur * m_flags.size() + top] = val;
    }
}

