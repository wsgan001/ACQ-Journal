set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1=[1100000	600000	319043.1768	184220.382	100745.1895];
y2=[1800000	900000	434064.4871	249499.2551	108017.1886];
y3=[2133.174209	1453.292591	1052.577481	942.5157972	1001.541422];
y4=[1055.989717	1030.605853	837.7028756	827.7889401	807.1749795];
y5=[196.202182	194.6173964	171.2638637	132.0716968	98.83575388];

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

axis([0.5 5.5 0.0 10000000]);

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