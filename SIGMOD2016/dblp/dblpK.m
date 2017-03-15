set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [116947.7041	87104.17442	41145.21626	29266.28984	21220.00588];
y2 = [218377.9205	202139.8591	128978.6456	121081.885	109674.6062];
y3 = [5720.999618	2074.961622	1410.909602	1102.100302	411.7221082];
y4 = [4862.346676	1757.200302	933.710148	728.538995	230.5936899];
y5 = [32.94125499	26.27293456	21.99730351	18.48506088	14.0260538];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kv');
hold on;
p4 = semilogy(x, y4, '-ks');
hold on;
p5 = semilogy(x, y5, '-kd');
hold on;

xlabel('k');
ylabel('time (ms)');

axis([0.5 5.5 0.0 30000000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'4','5','6','7','8'});
%leg=legend('basic-g','basic-w','Inc-S','Inc-T','Dec', 1);
%set(leg,'edgecolor','white');

set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'FontSize', 12);
leg1 = legend([p1, p2],'basic-g','basic-w');
set(leg1,'edgecolor','white');

set(gca, 'LineWidth', 1.5);
ah=axes('position',get(gca,'position'),'visible','off');
set(gca, 'FontSize', 12);
leg2 = legend(ah,[p3, p4, p5],'Inc-S','Inc-T','Dec');
set(leg2,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);


